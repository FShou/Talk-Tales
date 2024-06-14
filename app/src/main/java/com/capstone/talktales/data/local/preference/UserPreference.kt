package com.capstone.talktales.data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.talktales.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val PREF_NAME = stringPreferencesKey("name")
    private val PREF_EMAIL = stringPreferencesKey("email")
    private val PREF_TOKEN = stringPreferencesKey("token")


    fun getLoginUser(): Flow<User> =
        dataStore.data.map {
            User(
                it[PREF_NAME] ?: "",
                it[PREF_EMAIL] ?: "",
                it[PREF_TOKEN] ?: ""
            )
        }


    suspend fun saveLoginUser(name:String, email:String, token: String) {
        dataStore.edit {
            it[PREF_NAME] = name
            it[PREF_EMAIL] = email
            it[PREF_TOKEN] = token
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