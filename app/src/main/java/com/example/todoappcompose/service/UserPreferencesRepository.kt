package com.example.todoappcompose.service

import com.example.todoappcompose.data.TodoQuery
import com.example.todoappcompose.data.UserPreferences
import kotlinx.coroutines.flow.Flow


interface UserPreferencesRepository {
    val userPreferences : Flow<UserPreferences>

    suspend fun updateTodoQuery(query: TodoQuery)
}
