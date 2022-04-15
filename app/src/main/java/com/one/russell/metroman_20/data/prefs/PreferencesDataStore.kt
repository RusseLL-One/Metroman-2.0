package com.one.russell.metroman_20.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.one.russell.metroman_20.data.serialize
import com.one.russell.metroman_20.domain.BeatType.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "data_store"
)

@Suppress("PropertyName")
class PreferencesDataStore(
    appContext: Context
) {
    val bpm = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("BPM"),
        defaultValue = 60
    )

    val beatTypes = PrefsValue(
        dataStore = appContext.dataStore,
        key = stringPreferencesKey("BEAT_TYPES"),
        defaultValue = listOf(ACCENT, BEAT, BEAT, BEAT).serialize()
    )

    val training_tempoIncreasing_startBpm = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_INCREASING_START_BPM"),
        defaultValue = 90
    )

    val training_tempoIncreasing_endBpm = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_INCREASING_END_BPM"),
        defaultValue = 140
    )

    val training_tempoIncreasing_byBars_everyBars = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_INCREASING_BY_BARS_EVERY_BAR"),
        defaultValue = 1
    )

    val training_tempoIncreasing_byBars_increaseOn = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_INCREASING_BY_BARS_INCREASE_ON"),
        defaultValue = 2
    )

    val training_tempoIncreasing_byTime_minutes = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_INCREASING_BY_TIME_MINUTES"),
        defaultValue = 2
    )

    val training_barDropping_randomly_chancePercent = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("BAR_DROPPING_RANDOMLY_CHANCE_PERCENT"),
        defaultValue = 50
    )

    val training_barDropping_byValue_ordinaryBarsCount = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT"),
        defaultValue = 1
    )

    val training_barDropping_byValue_mutedBarsCount = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT"),
        defaultValue = 1
    )

    val training_beatDropping_chancePercent = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("BEAT_DROPPING_CHANCE_PERCENT"),
        defaultValue = 50
    )
}