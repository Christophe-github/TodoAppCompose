package com.example.todoappcompose.ui.page.todolist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoappcompose.TodosSample
import com.example.todoappcompose.data.Todo
import com.example.todoappcompose.data.TodoPriority
import com.example.todoappcompose.ui.common.toColor
import com.example.todoappcompose.ui.theme.*
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


/**
 * A composable drawing a list element with a _Todo.
 * This composable can be swiped out
 * @sample com.example.todoappcompose.page.todolist.TodoItem
 */

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun TodoItem(
    todo: Todo,
    selected: Boolean = false,
    onclick: (() -> Unit) = {},
    onLongClick: (() -> Unit) = {},
    onDismiss: (() -> Unit) = {}
) {
    //////////////////////////////////////////////////////
    // This Box is used as a container for the dismissable TodoItemContent
    //////////////////////////////////////////////////////
    BoxWithConstraints(
        Modifier.background(
            Brush.horizontalGradient(
                //Red to orange gradient
                listOf(
                    Color(0xFFFF3300),
                    Color(0xFFFF9933)
                )
            )
        )
    ) {

        val dismissState = rememberDismissState(DismissValue.DismissedToStart)
        val widthPx = with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx() }

        if (dismissState.isDismissed(DismissDirection.StartToEnd))
            onDismiss()

        //////////////////////////////////////////////////////
        // The remove icon is drawn first meaning the rest will be drawn above
        //////////////////////////////////////////////////////
        Icon(
            imageVector = Icons.Filled.Delete,
//            tint: White,
            contentDescription = "Delete",
            modifier = Modifier
                .padding(start = 20.dp)
                .size(36.dp)
                .align(Alignment.CenterStart)
        )

        //////////////////////////////////////////////////////
        // The actual content of the TodoItem
        //////////////////////////////////////////////////////

        Box(
            Modifier
                .todoDismissable(dismissState, widthPx)
                .combinedClickable(
                    onClick = onclick,
                    onClickLabel = "Edit todo",
                    onLongClick = onLongClick,
                    onLongClickLabel = "Mark as complete"
                )
                .background(MaterialTheme.colors.background)
        ) {
            TodoItemContent(todo, selected)
        }
    }
}


@ExperimentalMaterialApi
private fun Modifier.todoDismissable(dismissState: DismissState, widthPx: Float): Modifier =
    this.then(
        swipeable(
            state = dismissState,
            anchors = mapOf(
                0f to DismissValue.DismissedToStart,
                widthPx to DismissValue.DismissedToEnd
            ),
            orientation = Orientation.Horizontal,
            thresholds = { _, _ -> FractionalThreshold(0.5f) }
        ).then(
            offset { IntOffset(dismissState.offset.value.roundToInt(), 0) }
        )
    )

/**
 * The actual content of the TodoItem.
 * If [isSelected] is true, the background will be a semi transparent primary color
 */
@Composable
private fun TodoItemContent(todo: Todo, isSelected: Boolean) {

    val selectedColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.3f) else Color.Transparent
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(selectedColor).padding(16.dp).fillMaxWidth()
    ) {

        //////////////////////////////////////////////////////
        // Leading icon
        //////////////////////////////////////////////////////
        TodoItemIcon(todo.priority, todo.completed)


        //////////////////////////////////////////////////////
        // Title and description
        //////////////////////////////////////////////////////
        Column(Modifier.padding(horizontal = 24.dp).weight(1f)) {
            Text(
                text = todo.title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Text(
                text = todo.description,
                style = MaterialTheme.typography.subtitle1
            )
        }

        //////////////////////////////////////////////////////
        // Trailing date and time
        //TODO Maybe do the formatting and time zone in the viewmodel
        //////////////////////////////////////////////////////
        val deadline by rememberUpdatedState(
            ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(todo.deadline),
                ZoneId.systemDefault()
            )
        )

        val deadline1 = deadline.format(DateTimeFormatter.ofPattern("dd MMM"))
        val deadline2 = deadline.format(DateTimeFormatter.ofPattern("HH:mm"))


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = deadline1, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = deadline2, fontSize = 15.sp, modifier = Modifier.alpha(0.7f))
        }
    }
}


/**
 * The leading icon of the [TodoItem], consisting of a colored circle
 * able to flip and show a checkmark when the [completed] value is true
 */
@Composable
private fun TodoItemIcon(priority: TodoPriority, completed: Boolean) {
    val durationMillis = 400

    val rotation by animateFloatAsState(
        targetValue = if (completed) 0f else -180f,
        animationSpec = tween(durationMillis)
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer(rotationY = rotation)
            .background(priority.toColor(), CircleShape)
    ) {

        //The checkmark becomes visible when half a flip has been made
        if (rotation > -90)
            Icon(
                Icons.Filled.Check,
                tint = Color.White,
                modifier = Modifier.size(36.dp).align(Alignment.Center),
                contentDescription = "Completed")
    }
}


internal class TodoProvider : CollectionPreviewParameterProvider<Todo>(TodosSample)

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun TodoItemPreview(@PreviewParameter(TodoProvider::class) todo: Todo) {
    ToDoAppComposeTheme {
        TodoItem(todo, selected = todo.id == 2)
    }
}