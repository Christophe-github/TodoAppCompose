package com.example.todoappcompose.service.impl

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.todoappcompose.data.*
import com.example.todoappcompose.data.UserPreferences
import com.example.todoappcompose.service.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class UserPreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {


    private object PrefKeys {
        val TODO_PAGE = intPreferencesKey("TODO_PAGE")
        val TODO_ITEMS_PER_PAGE = intPreferencesKey("TODO_ITEMS_PER_PAGE")
        val TODO_SORT_BY = stringPreferencesKey("TODO_SORT_BY")
        val TODO_PRIORITY = stringPreferencesKey("TODO_PRIORITY")
        val TODO_COMPLETED = booleanPreferencesKey("TODO_COMPLETED")
    }


    override val userPreferences: Flow<UserPreferences> =
        dataStore.data.map { prefs -> mapUserPreferences(prefs) }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())


    private fun mapUserPreferences(prefs: Preferences): UserPreferences {
        val todoSortBy = prefs[PrefKeys.TODO_SORT_BY]?.let { TodoSortBy.valueOf(it) }
        val page = prefs[PrefKeys.TODO_PAGE]
        val itemsPerPage = prefs[PrefKeys.TODO_ITEMS_PER_PAGE]
        val todoPriority = prefs[PrefKeys.TODO_PRIORITY]?.let { TodoPriority.valueOf(it) }
        val todoCompleted = prefs[PrefKeys.TODO_COMPLETED]

        val query = TodoQuery(
            sortBy = todoSortBy ?: TodoQuery.DEFAULT.sortBy,
            page = page ?: TodoQuery.DEFAULT.page,
            itemsPerPage = itemsPerPage ?: TodoQuery.DEFAULT.itemsPerPage,
            filter = TodoFilter(
                priority = todoPriority,
                completed = todoCompleted
            )
        )

        return UserPreferences(query)
    }


    override suspend fun updateTodoQuery(query: TodoQuery) {
        dataStore.edit { preferences ->
            preferences[PrefKeys.TODO_SORT_BY] = query.sortBy.name
            preferences[PrefKeys.TODO_PAGE] = query.page
            preferences[PrefKeys.TODO_ITEMS_PER_PAGE] = query.itemsPerPage
            preferences.update(PrefKeys.TODO_PRIORITY,query.filter.priority?.name)
            preferences.update(PrefKeys.TODO_COMPLETED,query.filter.completed)
        }

    }

    private fun <T> MutablePreferences.update(key: Preferences.Key<T>, value: T?) {
        if (value != null)
            this[key] = value
        else
            this.remove(key)
    }

}
