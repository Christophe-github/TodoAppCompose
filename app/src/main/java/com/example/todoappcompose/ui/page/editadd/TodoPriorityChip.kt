package com.example.todoappcompose.ui.page.editadd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.todoappcompose.data.TodoPriority
import com.example.todoappcompose.ui.common.MySelectableChip
import com.example.todoappcompose.ui.common.toColor
import com.example.todoappcompose.ui.common.toLocaleString
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme


@ExperimentalMaterialApi
@Composable
fun TodoPriorityChip(priority : TodoPriority, modifier: Modifier = Modifier, checked : Boolean = false, onClick: () -> Unit = {}) =
    MySelectableChip(
        text = priority.toLocaleString(),
        selected = checked,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = { Box(Modifier.size(24.dp).background(priority.toColor(), CircleShape)) }
        )



internal class PriorityProvider : CollectionPreviewParameterProvider<TodoPriority>(
    listOf(TodoPriority.HIGH,TodoPriority.MEDIUM,TodoPriority.LOW,TodoPriority.NONE))

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun TodoPriorityChipPreview(@PreviewParameter(PriorityProvider::class) priority: TodoPriority) {
    ToDoAppComposeTheme {
        TodoPriorityChip(priority)
    }
}