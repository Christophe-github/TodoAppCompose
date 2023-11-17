package com.example.todoappcompose.ui.page.todolist

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todoappcompose.R
import com.example.todoappcompose.data.TodoFilter
import com.example.todoappcompose.data.TodoPriority
import com.example.todoappcompose.data.TodoQuery
import com.example.todoappcompose.data.TodoSortBy
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class TodoFilterBottomSheetKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var ctx: Context

    @org.junit.Before
    fun setup() {
        ctx = ApplicationProvider.getApplicationContext()
    }

    //Utility function to distinct drop down items from other objects
    private fun isDropdownItemAndHasText(text: String) = hasAnyAncestor(isPopup()) and hasText(text)


    @Test
    fun todoFilterbottomSheet() {
        val query = mutableStateOf(TodoQuery(TodoSortBy.Default, 1, 20, filter = TodoFilter(null, null)))

        composeTestRule.setContent {

            ToDoAppComposeTheme {
                TodoFilterBottomSheet(
                    query = query.value,
                    onQueryChanged = { query.value = it}
                )
            }
        }



        // Testing Sort by choosing

        with(composeTestRule) {
            // Clicking the drop down to make the options appear
            onNodeWithTag("BOTTOM_SHEET_SORT_BY").performClick()

            // Choosing priority option from drop down twice
            onNode(isDropdownItemAndHasText(ctx.getString(R.string.sort_priority))).performClick()
            assertEquals(TodoSortBy.Priority, query.value.sortBy)

            onNodeWithTag("BOTTOM_SHEET_SORT_BY").performClick()
            onNode(isDropdownItemAndHasText(ctx.getString(R.string.sort_priority))).performClick()
            assertEquals(TodoSortBy.Priority, query.value.sortBy)


            // Choosing deadline option from drop down
            onNodeWithTag("BOTTOM_SHEET_SORT_BY").performClick()
            onNode(isDropdownItemAndHasText(ctx.getString(R.string.sort_deadline))).performClick()
            assertEquals(TodoSortBy.Deadline, query.value.sortBy)


            // Test for priority

            onNodeWithText(ctx.getString(R.string.priority_high)).performClick()
            assertEquals(TodoPriority.HIGH, query.value.filter.priority)

            onNodeWithText(ctx.getString(R.string.priority_medium)).performScrollTo().performClick()
            assertEquals(TodoPriority.MEDIUM, query.value.filter.priority)

            onNodeWithText(ctx.getString(R.string.priority_low)).performScrollTo().performClick()
            assertEquals(TodoPriority.LOW, query.value.filter.priority)

            onNodeWithText(ctx.getString(R.string.priority_none)).performScrollTo().performClick()
            assertEquals(TodoPriority.NONE, query.value.filter.priority)

            onNode(
                hasText(ctx.getString(R.string.priority_all)) and
                        hasParent(hasTestTag("BOTTOM_SHEET_PRIORITY"))
            ).performScrollTo().performClick()
            assertNull(query.value.filter.priority)


            // Test for completed


            onNodeWithText(ctx.getString(R.string.completed)).performClick()
            assertEquals(true, query.value.filter.completed)

            onNodeWithText(ctx.getString(R.string.uncompleted)).performClick()
            assertEquals(false, query.value.filter.completed)

            onNode(
                hasText(ctx.getString(R.string.completed_all)) and
                        hasParent(hasTestTag("BOTTOM_SHEET_COMPLETED"))
            ).performScrollTo().performClick()
            assertNull(query.value.filter.completed)

        }

    }
}