package com.example.todoappcompose

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.todoappcompose.data.database.TodoDatabase
import com.example.todoappcompose.data.datastore.dataStore
import com.example.todoappcompose.service.TodoRepository
import com.example.todoappcompose.service.impl.TodoRepositoryImpl
import com.example.todoappcompose.service.UserPreferencesRepository
import com.example.todoappcompose.service.impl.UserPreferencesRepositoryImpl

object ServiceLocator {

    /**
     * Should not be set or used outside of test scope
     */
    @Volatile
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var todoRepository : TodoRepository? = null
        @VisibleForTesting set

    fun getTodoRepository(context : Context) : TodoRepository {
        return todoRepository ?: synchronized(this){

            var repo = todoRepository
            if (repo != null)
                return repo

            repo = createTodoRepository(context)
            todoRepository = repo
            return repo
        }
    }


    private fun createTodoRepository(context: Context) : TodoRepository {
        val localDataSource = createLocalDataSource(context)
        return TodoRepositoryImpl(localDataSource.TodoDAO())
    }

    private fun createLocalDataSource(context: Context) : TodoDatabase {
        return Room.databaseBuilder(context,
            TodoDatabase::class.java,
            "todo_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun getUserPreferencesRepository(context: Context) : UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(context.dataStore)
    }
}