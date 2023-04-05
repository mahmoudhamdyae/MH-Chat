package com.mahmoudhamdyae.mhchat.data.services

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Concrete class implementation to access data store
 */
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
        const val TAG = "PreferencesRepo"
    }

    val isFirstTime: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("hahaha", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            if (preferences[IS_FIRST_TIME] == null || preferences[IS_FIRST_TIME] == true) {
                setFirstTimePreference()
                true
            } else {
                false
            }
        }

    suspend fun setFirstTimePreference() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME] = false
        }
    }
}