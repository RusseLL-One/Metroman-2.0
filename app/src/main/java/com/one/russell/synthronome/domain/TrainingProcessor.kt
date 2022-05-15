package com.one.russell.synthronome.domain

import com.one.russell.synthronome.domain.providers.BeatTypesProvider
import com.one.russell.synthronome.domain.providers.BpmProvider
import com.one.russell.synthronome.domain.providers.TrainingStateProvider
import com.one.russell.synthronome.domain.wrappers.Clicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job
import kotlin.coroutines.coroutineContext
import kotlin.math.ceil
import kotlin.random.Random

class TrainingProcessor(
    private val clicker: Clicker,
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val trainingStateProvider: TrainingStateProvider
) {
    private var runningJob: Job? = null

    suspend fun startTraining(trainingData: TrainingData) {
        this.runningJob?.cancel()
        this.runningJob = coroutineContext.job

        trainingStateProvider.trainingState.value = TrainingState.Running(
            calcTrainingTime(trainingData = trainingData),
            trainingData is TrainingData.TempoIncreasing
        )

        when (trainingData) {
            is TrainingData.TempoIncreasing.ByBars ->
                processTempoIncreasingByBars(trainingData)
            is TrainingData.TempoIncreasing.ByTime ->
                processTempoIncreasingByTime(trainingData)
            is TrainingData.BarDropping.Randomly ->
                processBarDroppingRandomly(trainingData)
            is TrainingData.BarDropping.ByValue ->
                processBarDroppingByValue(trainingData)
            is TrainingData.BeatDropping ->
                processBeatDropping(trainingData)
        }
    }

    private fun calcTrainingTime(trainingData: TrainingData): Long = trainingData.run {
        return when (this) {
            is TrainingData.TempoIncreasing.ByBars -> {
                var totalTime = 0f
                val beatsInBar = beatTypesProvider.beatTypesFlow.value.size
                val bpmInterval = (endBpm - startBpm).toFloat()
                val increasesCount = ceil(bpmInterval / (everyBars * increaseOn)).toInt()
                for (increaseIndex in 0..increasesCount) {
                    val bpm = (startBpm + increaseOn * increaseIndex).coerceAtMost(endBpm)
                    val beatTimeMs = 1000f * 60f / bpm
                    totalTime += beatTimeMs * beatsInBar * everyBars
                }
                totalTime.toLong()
            }
            is TrainingData.TempoIncreasing.ByTime -> {
                var totalTime = 0f
                val beatsInBar = beatTypesProvider.beatTypesFlow.value.size
                val bpmInterval = (endBpm - startBpm).toFloat()
                val increasesCount = ceil(bpmInterval / increaseOn).toInt()
                for (increaseIndex in 0..increasesCount) {
                    val bpm = (startBpm + increaseOn * increaseIndex).coerceAtMost(endBpm)
                    val beatTimeMs = 1000f * 60f / bpm
                    totalTime += (beatTimeMs * beatsInBar) * ceil((everySeconds * 1000f) / (beatTimeMs * beatsInBar))
                }
                totalTime.toLong()
            }
            else -> TRAINING_TIME_INFINITE
        }
    }

    private suspend fun processTempoIncreasingByBars(
        trainingData: TrainingData.TempoIncreasing.ByBars
    ) {
        fun calcNewBpm(click: Clicker.Click, trainingData: TrainingData.TempoIncreasing.ByBars): Int {
            return if (trainingData.startBpm < trainingData.endBpm) {
                (click.bpm + trainingData.increaseOn)
                    .coerceIn(trainingData.startBpm, trainingData.endBpm)
            } else {
                (click.bpm - trainingData.increaseOn)
                    .coerceIn(trainingData.endBpm, trainingData.startBpm)
            }
        }

        bpmProvider.bpmFlow.emit(trainingData.startBpm) // Send initial bpm value
        var barsPassed = 0

        clicker.onClicked.collectWithActionQueueHandler { click, aqh ->
            if (click.bpm == trainingData.endBpm && click.isNextBeatFirst)
                aqh.queueAction { stopTraining() }

            if (click.isNextBeatFirst) barsPassed++

            if (barsPassed == trainingData.everyBars) {
                val newBpm = calcNewBpm(click, trainingData)
                clicker.setBpm(newBpm)
                aqh.queueAction { bpmProvider.bpmFlow.emit(newBpm) }
                barsPassed = 0
            }
        }
    }

    private suspend fun processTempoIncreasingByTime(
        trainingData: TrainingData.TempoIncreasing.ByTime
    ) {
        fun calcNewBpm(click: Clicker.Click, trainingData: TrainingData.TempoIncreasing.ByTime): Int {
            return if (trainingData.startBpm < trainingData.endBpm) {
                (click.bpm + trainingData.increaseOn)
                    .coerceIn(trainingData.startBpm, trainingData.endBpm)
            } else {
                (click.bpm - trainingData.increaseOn)
                    .coerceIn(trainingData.endBpm, trainingData.startBpm)
            }
        }

        bpmProvider.bpmFlow.emit(trainingData.startBpm) // Send initial bpm values
        var startTime = System.currentTimeMillis()
        var timePassed: Long

        clicker.onClicked.collectWithActionQueueHandler { click, aqh ->
            if (click.bpm == trainingData.endBpm && click.isNextBeatFirst) // todo
                aqh.queueAction { stopTraining() }

            timePassed = System.currentTimeMillis() - startTime

            if (click.isNextBeatFirst && timePassed > trainingData.everySeconds * 1000L) {
                val newBpm = calcNewBpm(click, trainingData)
                clicker.setBpm(newBpm)
                aqh.queueAction { bpmProvider.bpmFlow.emit(newBpm) }
                startTime = System.currentTimeMillis()
                timePassed = 0L
            }
        }
    }

    private suspend fun processBarDroppingRandomly(
        trainingData: TrainingData.BarDropping.Randomly
    ) {
        var isBarMuted = false
        clicker.onClicked.collect { click ->
            if (click.isNextBeatFirst) isBarMuted = isMuted(trainingData.chancePercent)
            if (isBarMuted) clicker.setNextBeatType(BeatType.MUTE)
        }
    }

    private suspend fun processBarDroppingByValue(
        trainingData: TrainingData.BarDropping.ByValue
    ) {
        var barsPassed = 0
        val ordinaryBarsCount = trainingData.ordinaryBarsCount
        val totalBarsCount = trainingData.ordinaryBarsCount + trainingData.mutedBarsCount
        clicker.onClicked.collect { click ->
            if (click.isNextBeatFirst) barsPassed++
            if (barsPassed == totalBarsCount) barsPassed = 0
            if (barsPassed >= ordinaryBarsCount) clicker.setNextBeatType(BeatType.MUTE)
        }
    }

    private suspend fun processBeatDropping(
        trainingData: TrainingData.BeatDropping
    ) {
        clicker.onClicked.collect {
            if (isMuted(trainingData.chancePercent))
                clicker.setNextBeatType(BeatType.MUTE)
        }
    }

    private fun isMuted(chancePercent: Int): Boolean {
        return Random.nextInt(101) <= chancePercent
    }

    fun stopTraining() {
        runningJob?.cancel()
        trainingStateProvider.trainingState.value = TrainingState.Idle
    }

    private suspend inline fun <T> Flow<T>.collectWithActionQueueHandler(crossinline action: suspend (value: T, aqh: ActionQueueHandler) -> Unit) {
        val actionQueueHandler = ActionQueueHandler()
        collect {
            actionQueueHandler.executeAll()
            action(it, actionQueueHandler)
        }
    }

    private class ActionQueueHandler {
        private val actionsList: MutableList<suspend () -> Unit> = mutableListOf()

        suspend fun executeAll() {
            actionsList.forEach { it() }
            actionsList.clear()
        }

        fun queueAction(action: suspend () -> Unit) {
            actionsList.add(action)
        }
    }

    companion object {
        const val TRAINING_TIME_INFINITE = -1L
    }
}