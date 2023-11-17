package com.example.todoappcompose.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.todoappcompose.R
import com.example.todoappcompose.data.TodoPriority
import com.example.todoappcompose.data.TodoSortBy
import com.example.todoappcompose.ui.theme.ExtendedTheme

@Composable
fun TodoPriority.toColor(): Color =
    when (this) {
        TodoPriority.HIGH -> ExtendedTheme.colors.priorityHigh
        TodoPriority.MEDIUM -> ExtendedTheme.colors.priorityMedium
        TodoPriority.LOW -> ExtendedTheme.colors.priorityLow
        TodoPriority.NONE -> ExtendedTheme.colors.priorityNone
    }

@Composable
fun TodoPriority.toLocaleString(): String =
    when (this) {
        TodoPriority.HIGH -> stringResource(R.string.priority_high)
        TodoPriority.MEDIUM -> stringResource(R.string.priority_medium)
        TodoPriority.LOW -> stringResource(R.string.priority_low)
        TodoPriority.NONE -> stringResource(R.string.priority_none)
    }


@Composable
fun TodoSortBy.toLocaleString(): String =
    when (this) {
        TodoSortBy.Default -> stringResource(R.string.sort_default)
        TodoSortBy.Deadline -> stringResource(R.string.sort_deadline)
        TodoSortBy.Priority -> stringResource(R.string.sort_priority)
    }


fun <T> LiveData<out T>.ignoreFirstChange() : LiveData<T> {
    var firstChange = true
    return MediatorLiveData<T>().apply {
        addSource(this@ignoreFirstChange) {
            if (firstChange)
                firstChange = false
            else {
                value = it
            }
        }
    }
}