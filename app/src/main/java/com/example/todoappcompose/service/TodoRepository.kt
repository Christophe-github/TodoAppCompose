package com.example.todoappcompose.service

import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoQuery
import kotlinx.coroutines.flow.Flow


interface TodoRepository {

    //Requests to GET Todos

    suspend fun insert(todo : Todo) : Long
    suspend fun getTodos() : List<Todo>
    suspend fun getTodo(id : Int) : Todo
    fun observeTodos() : Flow<List<Todo>>
    fun observeTodos(query: TodoQuery) : Flow<List<Todo>>


    //Requests to UPDATE Todos

    suspend fun update(vararg todo : Todo)


    //Requests to DELETE Todos

    suspend fun delete(vararg todo : Todo)
}