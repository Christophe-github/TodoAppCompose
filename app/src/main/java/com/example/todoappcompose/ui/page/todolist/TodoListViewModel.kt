package com.example.todoappcompose.ui.page.todolist

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.map
import com.example.todoappcompose.data.*
import com.example.todoappcompose.service.TodoRepository
import com.example.todoappcompose.service.UserPreferencesRepository
import com.example.todoappcompose.ui.common.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.pow


@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val todoRepository: TodoRepository,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val query = userPreferencesRepository.userPreferences
        .map { it.todoQuery }
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoQuery.DEFAULT)

    val isFilterActive = query.map { it.filter.priority != null || it.filter.completed != null }

    val todos = query
        .flatMapLatest { todoRepository.observeTodos(it) }
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())


    //DataStore from Userpreferences does not emit a new object in the flow if nothing changed.
    //This means there is a possibility to be stuck in endless loading if we rely on the todos flow
    //to reset the loading state to false because it may never be triggered.
    //In order to implement a loading state, chained flows are not the greatest solution
    //val loading : MutableStateFlow(false)


    private val _todoDeleted: MutableLiveData<Event<Todo>> = MutableLiveData()
    val todoDeleted: LiveData<Event<Todo>> = _todoDeleted


    fun updateTodoQuery(query: TodoQuery) = viewModelScope.launch {
        userPreferencesRepository.updateTodoQuery(query)
    }


    fun toggleCompleted(todo: Todo) = viewModelScope.launch {
        todoRepository.update(todo.copy(completed = !todo.completed))
    }


    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.delete(todo)
        _todoDeleted.postValue(Event(todo))
    }


}


class TodoListViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val todoRepository: TodoRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoListViewModel(userPreferencesRepository, todoRepository) as T
    }
}

