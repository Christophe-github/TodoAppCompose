package com.example.todoappcompose.ui.page.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.common.MyDropDown
import com.example.todoappcompose.ui.common.MySelectableChip
import com.example.todoappcompose.data.*
import com.example.todoappcompose.ui.common.toLocaleString
import com.example.todoappcompose.ui.page.editadd.TodoPriorityChip
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme


@ExperimentalMaterialApi
@Composable
fun TodoFilterBottomSheet(
    query: TodoQuery,
    onQueryChanged: (TodoQuery) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.background(color = MaterialTheme.colors.surface).padding(top = 16.dp, bottom = 32.dp)
    ) {

        ///////////////////////////////////////////////
        // The top gray bar to indicate the vertical scroll is possible
        ///////////////////////////////////////////////
        Box(
            Modifier
                .size(width = 100.dp, height = 5.dp)
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .align(Alignment.CenterHorizontally)
        )


        ///////////////////////////////////////////////
        // Input for "sortBy"
        ///////////////////////////////////////////////
        MyDropDown(
            label = "Sort by",
            options = TodoSortBy.values().map { it.toLocaleString() },
            selected = query.sortBy.toLocaleString(),
            onOptionSelect = { onQueryChanged(query.copy(sortBy = TodoSortBy.values()[it])) },
            modifier = Modifier.padding(horizontal = 16.dp).testTag("BOTTOM_SHEET_SORT_BY")
        )


        ///////////////////////////////////////////////
        // Input for "priority" filter
        ///////////////////////////////////////////////
        Row(
            Modifier.horizontalScroll(state = rememberScrollState())
                .testTag("BOTTOM_SHEET_PRIORITY")
                .semantics { contentDescription = "Filter by priority" }
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            MySelectableChip(
                text = stringResource(R.string.priority_all),
                selected = query.filter.priority == null,
                onClick = { onQueryChanged(query.copy(filter = query.filter.copy(priority = null))) }
            )

            TodoPriority.values().forEach {
                TodoPriorityChip(
                    priority = it,
                    checked = query.filter.priority == it,
                    onClick = { onQueryChanged(query.copy(filter = query.filter.copy(priority = it))) }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }


        ///////////////////////////////////////////////
        // Input for "completed" filter
        ///////////////////////////////////////////////
        Row(
            Modifier.horizontalScroll(state = rememberScrollState())
                .testTag("BOTTOM_SHEET_COMPLETED")
                .semantics { contentDescription = "Filter by completion" }
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            MySelectableChip(
                text = stringResource(R.string.completed_all),
                selected = query.filter.completed == null,
                onClick = { onQueryChanged(query.copy(filter = query.filter.copy(completed = null))) },
            )

            MySelectableChip(
                text = stringResource(R.string.completed),
                selected = query.filter.completed == true,
                onClick = { onQueryChanged(query.copy(filter = query.filter.copy(completed = true))) },
                leadingIcon = { Icon(painterResource(R.drawable.ic_check_24), "Completed") }
            )

            MySelectableChip(
                text = stringResource(R.string.uncompleted),
                selected = query.filter.completed == false,
                onClick = { onQueryChanged(query.copy(filter = query.filter.copy(completed = false))) },
                leadingIcon = { Icon(painterResource(R.drawable.ic_outline_pending_24), "Completed") }

            )

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
@ExperimentalMaterialApi
private fun TodoFilterBottomSheetPreview() {
    ToDoAppComposeTheme {

        TodoFilterBottomSheet(
            query = TodoQuery(
                sortBy = TodoSortBy.Priority,
                page = 1,
                itemsPerPage = 20,
                filter = TodoFilter(priority = null, completed = null)
            ),
            onQueryChanged = {}
        )
    }
}