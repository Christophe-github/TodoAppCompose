package com.example.todoappcompose.ui.page.todolist

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoappcompose.TodosSample
import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoPriority
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun TodoList(
    todos: List<Todo>,
    todosSelected: List<Todo> = listOf(),
    contentPadding: PaddingValues = PaddingValues(),
    onTodoClick: (Todo) -> Unit = {},
    onTodoLongClick: (Todo) -> Unit = {},
    onTodoDismiss: (Todo) -> Unit = {},
) {

    Box {
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        /////////////////////////////////////////////////
        // The actual list containing all the TodoItem and dividers
        /////////////////////////////////////////////////
        LazyColumn(
            state = listState,
            contentPadding = contentPadding,
            modifier = Modifier.semantics { contentDescription = "Todo list" }
        ) {
            items(todos, key = { it.id }) { todo ->
                Column(Modifier.animateItemPlacement()) {
                    TodoItem(
                        todo = todo,
                        selected = todosSelected.any { t -> t.id == todo.id },
                        onclick = { onTodoClick(todo) },
                        onLongClick = { onTodoLongClick(todo) },
                        onDismiss = { onTodoDismiss(todo) }
                    )
                    Divider()
                }
            }
        }

        /////////////////////////////////////////////////
        // A Button allowing scrolling back to top when at least one item
        // from the top is invisible
        /////////////////////////////////////////////////
        val showScrollTopButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

        AnimatedVisibility(
            visible = showScrollTopButton,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            ScrollTopButton(
                onClick = { scope.launch { listState.scrollToItem(0) } },
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}

@Composable
private fun ScrollTopButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
    ) {
        Icon(
            Icons.Filled.KeyboardArrowUp,
            tint = MaterialTheme.colors.primary,
            contentDescription = "Scroll to top"
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun TodoListPreview() {
    ToDoAppComposeTheme {
        TodoList(TodosSample, TodosSample.subList(1, 2))
    }
}

