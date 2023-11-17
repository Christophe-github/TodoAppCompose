package com.example.todoappcompose.ui.page.todolist

import com.example.todoappcompose.MainDispatcherRule
import com.example.todoappcompose.TodosSample
import com.example.todoappcompose.data.*
import com.example.todoappcompose.service.TodoRepository
import com.example.todoappcompose.service.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class TodoListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var vm: TodoListViewModel

    @org.junit.Before
    fun setup() {
        vm = TodoListViewModel(
            FakeUserPreferencesRepository(),
            FakeTodoRepository(),
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `Query update `() = runTest {
        val initialQuery = TodoQuery.DEFAULT

        // Create an empty collector for the StateFlow
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.query.collect() }


        vm.updateTodoQuery(initialQuery)
        assertEquals(initialQuery, vm.query.value)

        //Changing the filter with 'priority' to TodoPriority.HIGH and 'completed' to FALSE
        val newQuery = TodoQuery(
            TodoSortBy.Priority, 1, 20,
            TodoFilter(TodoPriority.HIGH, false)
        )

        vm.updateTodoQuery(newQuery)
        assertEquals(newQuery, vm.query.value)

        collectJob.cancel()

    }


    @Test
    fun `IsfilterActive`() = runTest {
        val isFilterActiveValues = mutableListOf<Boolean>()
        val collectJob =
            launch(UnconfinedTestDispatcher()) { vm.isFilterActive.toList(isFilterActiveValues) }

        val query = TodoQuery.DEFAULT

        vm.updateTodoQuery(query.copy(filter = TodoFilter(null, null)))
        // isFilterActive is false

        vm.updateTodoQuery(query.copy(filter = TodoFilter(TodoPriority.HIGH, false)))
        // isFilterActive is true

        vm.updateTodoQuery(query.copy(filter = TodoFilter(TodoPriority.MEDIUM, null)))
        // isFilterActive is true

        vm.updateTodoQuery(query.copy(filter = TodoFilter(null, null)))
        // isFilterActive is false

        vm.updateTodoQuery(query.copy(filter = TodoFilter(null, true)))
        // isFilterActive is true

        assertArrayEquals(
            "isFilterActive did not update properly after call to updateQuery()",
            arrayOf(false, true, true, false, true),
            isFilterActiveValues.toTypedArray()
        )

        collectJob.cancel()
    }


    @Test
    fun `Todos update with query`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.todos.collect() }

        val initialTodosSize = vm.todos.value.size

        var sortBy = TodoSortBy.Priority
        var priority = TodoPriority.HIGH
        var completed = false

        vm.updateTodoQuery(TodoQuery(sortBy, 1, 20, TodoFilter(priority, completed)))

        //There is the same number or fewer todos than before applying a filter
        assertTrue(vm.todos.value.size <= initialTodosSize)

        vm.todos.value.forEachIndexed { index, todo ->
            //Checks that the priority sort is working
            if (index < vm.todos.value.size - 1)
                assertTrue(todo.priority.isHigherOrEqualsThan(vm.todos.value[index + 1].priority))

            //Checks the filter is working
            assertEquals(priority, todo.priority)
            assertEquals(completed, todo.completed)
        }

        collectJob.cancel()
    }

    /*
       vm.updateTodoQuery(vm.query.getOrAwaitValue().copy(filter = vm.query.getOrAwaitValue().filter.copy(priority = TodoPriority.HIGH)))
       assertEquals(TodoPriority.HIGH, vm.query.getOrAwaitValue().filter.priority)
       assertTrue(vm.isFilterActive.getOrAwaitValue())

       vm.updateTodoQuery(vm.query.getOrAwaitValue().copy(filter = vm.query.getOrAwaitValue().filter.copy(completed = true)))
       assertEquals(true, vm.query.getOrAwaitValue().filter.completed)
       assertTrue(vm.isFilterActive.getOrAwaitValue())

       val todos = vm.todos.getOrAwaitValue()

       todos.forEach {
           assertEquals(TodoPriority.HIGH, it.priority)
           assertTrue(it.completed)
       }

       assertTrue(vm.isFilterActive.getOrAwaitValue())
*/
}


class FakeTodoRepository : TodoRepository {

    private val _todosDB = TodosSample.toMutableList()

    private val _todos = MutableStateFlow(_todosDB)
    private val todos: Flow<List<Todo>> = _todos


    override suspend fun insert(todo: Todo): Long {
        _todosDB.add(todo)
        _todos.value = _todosDB
        return 0
    }

    override suspend fun getTodos(): List<Todo> = _todosDB

    override suspend fun getTodo(id: Int): Todo = _todosDB.first { it.id == id }

    override fun observeTodos(): Flow<List<Todo>> = todos

    override fun observeTodos(query: TodoQuery): Flow<List<Todo>> =
        todos.map { todos ->
            var remaining = todos

            if (query.filter.priority != null)
                remaining = remaining.filter { it.priority == query.filter.priority }

            if (query.filter.completed != null)
                remaining = remaining.filter { it.completed == query.filter.completed }

            remaining.sortedBy { it.priority.value }
        }


    override suspend fun update(vararg todo: Todo) {
        val ids = todo.map { it.id }
        val concerned = _todosDB.filter { it.id in ids }

        _todosDB.apply {
            removeAll(concerned)
            addAll(todo)
        }
        _todos.value = _todosDB
    }


    override suspend fun delete(vararg todo: Todo) {
        _todosDB.removeAll(todo.toSet())
        _todos.value = _todosDB
    }

}

class FakeUserPreferencesRepository : UserPreferencesRepository {

    private val _userPreferences = MutableStateFlow(UserPreferences(TodoQuery.DEFAULT))
    override val userPreferences: Flow<UserPreferences> = _userPreferences


    override suspend fun updateTodoQuery(query: TodoQuery) {
//        _userPreferences.emit(_userPreferences.value.copy(todoQuery = query))
        _userPreferences.value = UserPreferences(query)
    }

}