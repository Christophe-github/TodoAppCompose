package com.example.todoappcompose.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(USER_PREFERENCES_NAME)
