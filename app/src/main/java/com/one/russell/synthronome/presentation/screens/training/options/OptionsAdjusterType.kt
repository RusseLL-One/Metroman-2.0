package com.one.russell.synthronome.presentation.screens.training.options

import androidx.annotation.StringRes
import com.one.russell.synthronome.R
import com.one.russell.synthronome.domain.Constants

enum class OptionsAdjusterType(
    val minValue: Int,
    val maxValue: Int,
    val step: Int = 1,
    val controlType: ControlType,
    @StringRes val titleRes: Int
) {
    TEMPO_CHANGE_START_BPM(
        minValue = Constants.MIN_BPM,
        maxValue = Constants.MAX_BPM,
        controlType = ControlType.KNOB,
        titleRes = R.string.training_tempoChange_startBpm
    ),
    TEMPO_CHANGE_END_BPM(
        minValue = Constants.MIN_BPM,
        maxValue = Constants.MAX_BPM,
        controlType = ControlType.KNOB,
        titleRes = R.string.training_tempoChange_endBpm
    ),
    TEMPO_CHANGE_STEP(
        minValue = 1,
        maxValue = Constants.MAX_BPM,
        controlType = ControlType.KNOB,
        titleRes = R.string.training_tempoChange_increaseOn
    ),
    TEMPO_CHANGE_BY_BARS_EVERY_BARS(
        minValue = 1,
        maxValue = 16,
        controlType = ControlType.PICKER,
        titleRes = R.string.training_tempoChange_everyBars
    ),
    TEMPO_CHANGE_BY_TIME_EVERY_SECONDS(
        minValue = 1,
        maxValue = 60,
        controlType = ControlType.PICKER,
        titleRes = R.string.training_tempoChange_everySeconds
    ),
    BAR_DROPPING_RANDOMLY_CHANCE(
        minValue = 0,
        maxValue = 100,
        step = 5,
        controlType = ControlType.PICKER,
        titleRes = R.string.training_barDropping_chance
    ),
    BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT(
        minValue = 1,
        maxValue = 16,
        controlType = ControlType.PICKER,
        titleRes = R.string.training_barDropping_ordinaryBarsCount
    ),
    BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT(
        minValue = 1,
        maxValue = 16,
        controlType = ControlType.PICKER,
        titleRes = R.string.training_barDropping_mutedBarsCount
    ),
    BEAT_DROPPING_CHANCE(
        minValue = 0,
        maxValue = 100,
        step = 5,
        controlType = ControlType.PICKER,
        titleRes = R.string.training_beatDropping_chance
    )
}

enum class ControlType {
    KNOB,
    PICKER
}