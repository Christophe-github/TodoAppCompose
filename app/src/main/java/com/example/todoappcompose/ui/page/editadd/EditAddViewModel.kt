package com.example.todoappcompose.ui.page.editadd

import android.util.Log
import androidx.lifecycle.*
import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.service.TodoRepository
import com.example.todoappcompose.ui.common.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class EditAddViewModel(
    val isEditMode: Boolean,
    private val todoId: String? = null,
    private val todoRepository: TodoRepository
) : ViewModel() {

    val isAddMode: Boolean
        get() = !isEditMode


    init {
        if (isEditMode)
            requireNotNull(todoId)
    }


    private val _todo = MutableStateFlow(
        Todo.create(
            deadline = ZonedDateTime
                .now()
                .plusDays(1)
                .withHour(9)
                .withMinute(0)
                .toEpochSecond()
        )
    )
    val todo: Flow<Todo> = _todo

    private val _editTodoNotFound = MutableStateFlow(false)
    val editTodoNotFound: Flow<Boolean> = _editTodoNotFound

    private val _todoSaved = MutableStateFlow<Event<Todo>?>(null)
    val todoSaved: Flow<Event<Todo>?> = _todoSaved

    init {
        if (isEditMode) {
            _editTodoNotFound.value = true

            viewModelScope.launch {
                try {
                    val todo = todoRepository.getTodo(todoId!!.toInt())
                    _todo.value = todo
                    _editTodoNotFound.value = false
                } catch (e: Exception) {
                    Log.d("#########", "$e")
                }
            }
        }
    }

    fun updateTodo(todo: Todo) {
        _todo.value = todo
    }

    fun saveTodo(todo: Todo) {
        viewModelScope.launch {
            if (isAddMode)
                todoRepository.insert(todo)
            else
                todoRepository.update(todo)

            _todoSaved.value = Event(todo)
        }
    }


}

class EditAddViewModelFactory(
    private val isEditMode: Boolean,
    private val todoId: String? = null,
    private val todoRepository: TodoRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditAddViewModel(isEditMode, todoId, todoRepository) as T
    }
}
