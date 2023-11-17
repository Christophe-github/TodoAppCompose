package com.example.todoappcompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController
import com.example.todoappcompose.ServiceLocator
import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoQuery
import com.example.todoappcompose.ui.page.editadd.EditAddPage
import com.example.todoappcompose.ui.page.editadd.EditAddViewModel
import com.example.todoappcompose.ui.page.editadd.EditAddViewModelFactory
import com.example.todoappcompose.ui.page.todolist.TodoListPage
import com.example.todoappcompose.ui.page.todolist.TodoListViewModel
import com.example.todoappcompose.ui.page.todolist.TodoListViewModelFactory



@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun TodolistRoute(navController: NavController) {
    val todoListviewModel = viewModel(
        modelClass = TodoListViewModel::class.java,
        factory = TodoListViewModelFactory(
            ServiceLocator.getUserPreferencesRepository(LocalContext.current),
            ServiceLocator.getTodoRepository(LocalContext.current)
        )
    )

    val todos by todoListviewModel.todos.collectAsStateWithLifecycle(listOf())
    val query by todoListviewModel.query.collectAsStateWithLifecycle(TodoQuery.DEFAULT)
    val isFilterActive by todoListviewModel.isFilterActive.collectAsStateWithLifecycle(false)
    val todoDeleted by todoListviewModel.todoDeleted.observeAsState()


    TodoListPage(
        todos = todos,
        query = query,
        isFilterActive = isFilterActive,
        todoDeleted = todoDeleted,
        onAddNewTodoClick = { navController.navigate("addTodo") },
        onEditTodoClick = { todo -> navController.navigate("editTodo/${todo.id}") },
        onToggleCompletedTodo = { todo -> todoListviewModel.toggleCompleted(todo) },
        onUpdateQuery = { todoListviewModel.updateTodoQuery(it) },
        onDeleteTodo = { todo -> todoListviewModel.deleteTodo(todo) }
    )
}



@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun EditAddTodoRoute(navController: NavController, edit : Boolean, todoId: String? = null) {
    val viewModel = viewModel(
        modelClass = EditAddViewModel::class.java,
        factory = EditAddViewModelFactory(
            isEditMode = edit,
            todoId,
            ServiceLocator.getTodoRepository(LocalContext.current)
        )
    )

    val isEditMode by remember { mutableStateOf(viewModel.isEditMode) }
    val editTodoNotFound by viewModel.editTodoNotFound.collectAsState(false)
    val todoSaved by viewModel.todoSaved.collectAsState(null)
    val todo by viewModel.todo.collectAsState(Todo.create())

    todoSaved?.getContentIfNotHandled()?.let {
        LaunchedEffect(it) {
            navController.popBackStack()
        }
    }

    EditAddPage(
        todo = todo,
        isEditMode = isEditMode,
        editTodoNotFound = editTodoNotFound,
        onBackPressed = navController::popBackStack ,
        onUpdateTodo = viewModel::updateTodo,
        onSaveTodo = viewModel::saveTodo
    )


}