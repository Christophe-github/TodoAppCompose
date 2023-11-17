package com.example.todoappcompose

import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoPriority
import java.time.LocalDate
import java.time.ZonedDateTime

val TodosSample = listOf(
    Todo(
        0,
        title = "Shopping",
        description = "Buy groceries",
        priority = TodoPriority.HIGH,
        deadline = ZonedDateTime.now().toEpochSecond(),
        false
    ),
    Todo(
        1,
        title = "Exercising",
        description = "Go to the swimming pool",
        priority = TodoPriority.MEDIUM,
        deadline = ZonedDateTime.now().toEpochSecond(),
        completed = true
    ),
    Todo(
        2,
        title = "Laundry",
        description = "Wash dirty clothes",
        priority = TodoPriority.LOW,
        deadline = ZonedDateTime.now().toEpochSecond(),
        false
    ),
    Todo(
        3,
        title = "Tidying",
        description = "Tidying the room",
        priority = TodoPriority.NONE,
        deadline = ZonedDateTime.now().toEpochSecond(),
        false
    ),
    Todo(
        4,
        title = "Something else",
        description = "Another high priority task",
        priority = TodoPriority.HIGH,
        deadline = ZonedDateTime.now().toEpochSecond(),
        true
    ),
)