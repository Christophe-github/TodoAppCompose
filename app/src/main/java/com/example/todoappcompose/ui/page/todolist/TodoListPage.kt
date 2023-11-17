package com.example.todoappcompose.ui.page.todolist

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoappcompose.R
import com.example.todoappcompose.TodosSample
import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoQuery
import com.example.todoappcompose.ui.common.Event
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TodoListPage(
    todos: List<Todo>,
    query: TodoQuery,
    isFilterActive: Boolean,
    todoDeleted: Event<Todo>?,
    onAddNewTodoClick: () -> Unit,
    onEditTodoClick: (Todo) -> Unit,
    onToggleCompletedTodo: (Todo) -> Unit,
    onDeleteTodo: (Todo) -> Unit,
    onUpdateQuery: (TodoQuery) -> Unit
) {

    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()



    //////////////////////////////////////////////////////////
    // Showing a message with the snackbar if a _Todo is deleted
    //////////////////////////////////////////////////////////
    todoDeleted?.getContentIfNotHandled()?.let {
        LaunchedEffect(it) {
            bottomSheetScaffoldState.snackbarHostState.showSnackbar("Todo deleted")
        }
    }


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        topBar = {
            TodoListAppBar(
                isFilterActive = isFilterActive,
                onFilterIconClick = { scope.launch { bottomSheetScaffoldState.bottomSheetState.expand() } })
        },
        snackbarHost = { SnackBarHost(bottomSheetScaffoldState) },
        floatingActionButton = { AddTodoFAB(onclick = onAddNewTodoClick) },
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetElevation = 8.dp,
        sheetPeekHeight = 50.dp,
        sheetContent = {
            TodoFilterBottomSheet(
                query = query,
                onQueryChanged = onUpdateQuery
            )
        },

        ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            //////////////////////////////////////////////////////////
            // The list of TodoItem
            //////////////////////////////////////////////////////////
            TodoList(
                todos = todos,
                contentPadding = PaddingValues(bottom = 100.dp),
                onTodoClick = onEditTodoClick,
                onTodoLongClick = onToggleCompletedTodo,
                onTodoDismiss = onDeleteTodo
            )


            //////////////////////////////////////////////////////////
            // A loading page appearing above the list
            //////////////////////////////////////////////////////////
            AnimatedVisibility(
                visible = false,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(BiasAlignment(0f, -0.5f)))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SnackBarHost(state: BottomSheetScaffoldState) {
    SnackbarHost(
        state.snackbarHostState,
        Modifier
            .padding(16.dp)
            .offset(y = (-80).dp)
//            .offset(y = (state.bottomSheetState.offset.value.dp / 1)  - 1920.dp)
    ) { data ->
        Snackbar(
            content = {
                Text(
                    text = data.message,
                )
            }
        )
    }
}

@Composable
private fun AddTodoFAB(onclick: () -> Unit) =
    FloatingActionButton(onClick = onclick) {
        Icon(
            Icons.Filled.Add,
//            tint = Color.White,
            modifier = Modifier.size(36.dp),
            contentDescription = "Add new todo"
        )
    }

/**
 * The App bar for the [TodoListPage], with a filter icon button.
 * If [isFilterActive] is true, the filter icon has a filled style,
 * otherwise it has an outlined style.
 */
@Composable
private fun TodoListAppBar(
    isFilterActive: Boolean,
    onFilterIconClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        //////////////////////////////////////////////////////////
        // The title of the app bar
        //////////////////////////////////////////////////////////
        Text(
            text = "To do list",
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                .semantics { heading() },
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )

        //////////////////////////////////////////////////////////
        // The trailing Icon of the app bar
        //////////////////////////////////////////////////////////
        IconButton(
            onClick = onFilterIconClick,
            modifier = Modifier.semantics {
                stateDescription = if (isFilterActive) "Active filter" else "Inactive filter"
            }
        ) {
            Box(Modifier.size(width = 24.dp, height = 28.dp)) {

                // Swaps the filled and outlined filter icon depending on isFilterActive
                Crossfade(targetState = isFilterActive, label="Crossfade filter icon") { isFilterActive ->
                    when (isFilterActive) {
                        false -> Icon(
                            painter = painterResource(R.drawable.ic_outline_filter_alt_24),
                            contentDescription = "Search todos",
                            tint = MaterialTheme.colors.primary
                        )
                        true -> Icon(
                            painter = painterResource(R.drawable.ic_baseline_filter_alt_24),
                            contentDescription = "Search todos",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }

                //////////////////////////////////////////////////////////
                // A Horizontal bar below the filled filter icon
                // to emphasize that the filter is active
                //////////////////////////////////////////////////////////

                androidx.compose.animation.AnimatedVisibility(
                    visible = isFilterActive,
                    enter = expandHorizontally(expandFrom = Alignment.CenterHorizontally),
                    exit = shrinkHorizontally(shrinkTowards = Alignment.CenterHorizontally),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Box(
                        Modifier
                            .background(MaterialTheme.colors.primary)
                            .size(height = 3.dp, width = 24.dp)
                    )
                }
            }
        }
    }

}


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun TodoListPagePreview() {
    ToDoAppComposeTheme {
        TodoListPage(
            todos = TodosSample,
            query = TodoQuery.DEFAULT,
            isFilterActive = false,
            todoDeleted = null,
            onAddNewTodoClick = { },
            onEditTodoClick = { },
            onToggleCompletedTodo = { },
            onUpdateQuery = { },
            onDeleteTodo = { }
        )
    }
}



