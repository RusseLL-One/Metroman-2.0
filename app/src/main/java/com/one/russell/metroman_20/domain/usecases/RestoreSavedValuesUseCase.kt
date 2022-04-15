package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.deserialize
import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import com.one.russell.metroman_20.domain.providers.BpmProvider

class RestoreSavedValuesUseCase(
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute() {
        bpmProvider.bpmFlow.emit(dataStore.bpm.getValue())
        beatTypesProvider.beatTypesFlow.emit(dataStore.beatTypes.getValue().deserialize())
    }
}