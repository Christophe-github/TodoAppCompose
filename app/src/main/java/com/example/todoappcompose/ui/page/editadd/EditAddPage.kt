package com.example.todoappcompose.ui.page.editadd

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoPriority
import com.example.todoappcompose.ui.common.showDatePickerDialog
import com.example.todoappcompose.ui.common.showTimePickerDialog
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme
import java.time.*
import java.time.format.DateTimeFormatter


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun EditAddPage(
    todo: Todo,
    isEditMode: Boolean,
    editTodoNotFound: Boolean,
    onBackPressed: () -> Unit,
    onUpdateTodo: (Todo) -> Unit,
    onSaveTodo: (Todo) -> Unit,
) {

//    val deadline = ZonedDateTime.ofInstant(
//        Instant.ofEpochSecond(todo.deadline),
//        ZoneId.systemDefault()
//    )

    val deadline by rememberUpdatedState(
        ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(todo.deadline),
            ZoneId.systemDefault()
        )
    )



    Scaffold(
        topBar = {
            EditAddTopAppBar(isEditMode, onBackPressed)
        },
        floatingActionButton = { EditAddFAB { onSaveTodo(todo) } }
    ) { padding ->


        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxWidth()
//                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 40.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            // Optional Mark as complete checkbox

            if (isEditMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    // We use toggleable to be able to click the whole row to check the Checkbox
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .toggleable(
                            value = todo.completed,
                            role = Role.Checkbox,
                            onValueChange = { onUpdateTodo(todo.copy(completed = it)) })
                        .padding(16.dp)
                ) {
                    Text("Mark as completed", style = MaterialTheme.typography.subtitle1)
                    Checkbox(checked = todo.completed, onCheckedChange = null)
                }
            }


            // Text input for title

            OutlinedTextField(
                todo.title,
                label = { Text("Title") },
                onValueChange = { onUpdateTodo(todo.copy(title = it)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            val keyboardController = LocalSoftwareKeyboardController.current

            // Text input for description

            OutlinedTextField(
                value = todo.description,
                label = { Text("Description") },
                onValueChange = { onUpdateTodo(todo.copy(description = it)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )


            // Grid for choosing priority

            Text(
                "Priority", style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp).semantics { heading() }
            )

            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.height(140.dp)
            ) {
                items(TodoPriority.values()) {
                    Box(Modifier.widthIn(min = 140.dp)) {
                        TodoPriorityChip(
                            priority = it,
                            checked = todo.priority == it,
                            onClick = { onUpdateTodo(todo.copy(priority = it)) },
                            modifier = Modifier.semantics { Role.RadioButton }
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            // Input for deadline

            Text(
                text = "Deadline",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp).semantics { heading() }
            )

            // Input for the day of the year


            Row(
                modifier = Modifier.widthIn(max = 300.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = deadline.format(DateTimeFormatter.ofPattern("d MMMM")),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                val ctx = LocalContext.current

                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showDatePickerDialog(
                            context = ctx,
                            initialDate = deadline.toLocalDate(),
                            onDateChange = { newDate ->
                                onUpdateTodo(
                                    todo.copy(
                                        deadline = deadline
                                            .withYear(newDate.year)
                                            .withMonth(newDate.month.value)
                                            .withDayOfMonth(newDate.dayOfMonth)
                                            .toEpochSecond()
                                    )
                                )
                            }
                        )
                    }
                ) {
                    Text("Change".uppercase(), style = MaterialTheme.typography.button)

                }
            }


            // Input for the hour of the day

            Row(
                modifier = Modifier.widthIn(max = 300.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = deadline.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                val ctx = LocalContext.current

                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showTimePickerDialog(
                            context = ctx,
                            initialTime = deadline.toLocalTime(),
                            onTimeChange = { newTime ->
                                onUpdateTodo(
                                    todo.copy(
                                        deadline = deadline
                                            .withHour(newTime.hour)
                                            .withMinute(newTime.minute)
                                            .toEpochSecond()
                                    )
                                )
                            }
                        )
                    }
                ) {
                    Text("Change".uppercase(), style = MaterialTheme.typography.button)
                }
            }

        }
    }


}


@Composable
fun EditAddTopAppBar(isEditMode: Boolean, onNavigationIconClick: () -> Unit = {}) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
//        elevation = 12.dp
    ) {
        IconButton(onClick = onNavigationIconClick) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Previous page",
                tint = MaterialTheme.colors.primary
            )
        }

        Text(
            modifier = Modifier.padding(8.dp).semantics { heading() },
            text = if (isEditMode) "Edit todo" else "Add todo",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )
    }
}


@Composable
fun EditAddFAB(onclick: () -> Unit) =
    FloatingActionButton(onclick) {
        Icon(
            Icons.Filled.Check,
            modifier = Modifier.size(36.dp),
            contentDescription = "Validate"
        )
    }


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun EditAddPagePreview() {
    ToDoAppComposeTheme {
        EditAddPage(
            todo = Todo.create(),
            isEditMode = false,
            editTodoNotFound = false,
            onBackPressed = {},
            onUpdateTodo = {},
            onSaveTodo = {}
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun EditAddPagePreview2() {
    ToDoAppComposeTheme {
        EditAddPage(
            todo = Todo.create(),
            isEditMode = true,
            editTodoNotFound = false,
            onBackPressed = {},
            onUpdateTodo = {},
            onSaveTodo = {}
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showBackground = true, widthDp = 320)
@Composable
private fun EditAddPagePreviewSmall() {
    ToDoAppComposeTheme {
        EditAddPage(
            todo = Todo.create(),
            isEditMode = false,
            editTodoNotFound = false,
            onBackPressed = {},
            onUpdateTodo = {},
            onSaveTodo = {}
        )
    }
}