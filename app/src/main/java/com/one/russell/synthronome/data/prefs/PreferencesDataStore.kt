package com.one.russell.synthronome.data.prefs

import android.content.Context
import android.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.one.russell.synthronome.R
import com.one.russell.synthronome.data.serialize
import com.one.russell.synthronome.domain.BeatType.*
import com.one.russell.synthronome.domain.Bookmark
import com.one.russell.synthronome.getColorCompat

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
        defaultValue = listOf(ACCENT, BEAT, SUBACCENT, BEAT).serialize()
    )

    val bookmarks = PrefsValue(
        dataStore = appContext.dataStore,
        key = stringPreferencesKey("BOOKMARKS"),
        defaultValue = emptyList<Bookmark>().serialize()
    )

    val isVibrationEnabled = PrefsValue(
        dataStore = appContext.dataStore,
        key = booleanPreferencesKey("IS_VIBRATION_ENABLED"),
        defaultValue = false
    )

    val isFlashEnabled = PrefsValue(
        dataStore = appContext.dataStore,
        key = booleanPreferencesKey("IS_FLASH_ENABLED"),
        defaultValue = false
    )

    val soundPresetId = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("SOUND_PRESET_ID"),
        defaultValue = 1
    )

    val training_tempoChange_startBpm = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_CHANGE_START_BPM"),
        defaultValue = 90
    )

    val training_tempoChange_endBpm = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_CHANGE_END_BPM"),
        defaultValue = 140
    )

    val training_tempoChange_step = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_CHANGE_STEP"),
        defaultValue = 2
    )

    val training_tempoChange_byBars_everyBars = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_CHANGE_EVERY_BARS"),
        defaultValue = 1
    )

    val training_tempoChange_byTime_everySeconds = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("TEMPO_CHANGE_BY_TIME_EVERY_SECONDS"),
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

    val color_primary = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("COLOR_PRIMARY"),
        defaultValue = appContext.getColorCompat(R.color.defaultPrimary)
    )

    val color_secondary = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("COLOR_SECONDARY"),
        defaultValue = appContext.getColorCompat(R.color.defaultSecondary)
    )

    val color_background = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("COLOR_BACKGROUND"),
        defaultValue = appContext.getColorCompat(R.color.defaultBackground)
    )

    val color_on_primary = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("COLOR_ON_PRIMARY"),
        defaultValue = Color.WHITE
    )

    val color_on_background = PrefsValue(
        dataStore = appContext.dataStore,
        key = intPreferencesKey("COLOR_ON_BACKGROUND"),
        defaultValue = Color.WHITE
    )
}