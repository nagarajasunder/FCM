package com.geekydroid.firebaselearn.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

object UserPreferences {


    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    val USER_ID = stringPreferencesKey("USER_ID")

    fun getUserId(applicationContext: Context): Flow<String> {
        return applicationContext.datastore.data.catch { exception ->
            if (exception is IOException) {
                emptyPreferences()
            } else {
                throw exception
            }
        }.map { preferences ->
           preferences[USER_ID] ?: "PODA"
        }
    }


    suspend fun updateUserID(userId: String, applicationContext: Context) {
        Log.d("pref", "updateUserID: $userId")
        try {
            applicationContext.datastore.edit { preferences ->
                preferences[USER_ID] = userId
            }
            getUserId(applicationContext).map {
                Log.d("pref", "updateUserID: $it")
            }
        } catch (e: Exception) {
            Log.d("pref", "updateUserID: ${e.message}")
        }
    }
}