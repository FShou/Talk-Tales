package com.capstone.talktales.data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val LOGIN_USER_KEY = stringPreferencesKey("user")

    fun getLoginUser(): Flow<String?> =
        dataStore.data.map {
            it[LOGIN_USER_KEY]
        }


    suspend fun saveLoginUser(token: String) {
        dataStore.edit {
            it[LOGIN_USER_KEY] = token
        }
    }

    suspend fun clearLoginUser() {
        dataStore.edit {
            it.clear()
        }
    }


    companion object {
        @Volatile
        private var instance: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return instance ?: synchronized(this) {
                instance ?: UserPreference(dataStore)
            }.also { instance = it }
        }
    }
}