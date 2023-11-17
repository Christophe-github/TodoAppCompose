package com.example.todoappcompose.service.impl

import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoQuery
import com.example.todoappcompose.data.database.TodoDAO
import com.example.todoappcompose.service.TodoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class TodoRepositoryImpl(private val todoDAO: TodoDAO) : TodoRepository {

    ////////////////////////////////////
    // Requests to GET Todos
    ////////////////////////////////////
    override suspend fun insert(todo: Todo) = todoDAO.insert(todo)
    override suspend fun getTodos() = todoDAO.getTodos()
    override suspend fun getTodo(id: Int) = todoDAO.getTodo(id)
    override fun observeTodos() = todoDAO.observeTodos()
    override fun observeTodos(query: TodoQuery) = todoDAO.observeTodos(query)

    ////////////////////////////////////
    // Requests to UPDATE Todos
    ////////////////////////////////////
    override suspend fun update(vararg todo: Todo) = todoDAO.update(*todo)

    ////////////////////////////////////
    //Requests to DELETE Todos
    ////////////////////////////////////
    override suspend fun delete(vararg todo: Todo) = todoDAO.delete(*todo)


}


