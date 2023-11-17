package com.example.todoappcompose.ui.common

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.todoappcompose.data.TodoSortBy
import com.example.todoappcompose.ui.page.todolist.TodoFilterBottomSheet
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterialApi
class MyDropDownKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun myDropDownTest() {
        val choice = mutableStateOf("")
        val options = listOf("a", "b", "c")

        composeTestRule.setContent {
            ToDoAppComposeTheme {
                MyDropDown(
                    label = "Dropdown",
                    selected = choice.value,
                    options = options,
                    onOptionSelect = { choice.value = options[it] },
                    modifier = Modifier.testTag("DROPDOWN")
                )
            }
        }


        //The drop down is not visible so the elements do not exist in the tree
        options.forEachIndexed { i, _ ->
            composeTestRule.onNodeWithTag("DROPDOWN_OPTION_$i").assertDoesNotExist()
        }

        // Clicking the drop down make these elements appear
        composeTestRule.onNodeWithTag("DROPDOWN").performClick()
        options.forEachIndexed { i, _ ->
            composeTestRule.onNodeWithTag("DROPDOWN_OPTION_$i").assertIsDisplayed()
        }


        // Choosing second element from drop down
        var position = 1
        composeTestRule.onNodeWithTag("DROPDOWN_OPTION_$position")
            .assertTextContains(options[position]).performClick()
        assertEquals(options[position], choice.value)

        // Choosing third from drop down
        position = 2
        composeTestRule.onNodeWithTag("DROPDOWN").performClick()
        composeTestRule.onNodeWithTag("DROPDOWN_OPTION_$position")
            .assertTextContains(options[position]).performClick()
        assertEquals(options[position], choice.value)

        //The drop down is not visible anymore so the elements do not exist in the tree
        options.forEachIndexed { i, _ ->
            composeTestRule.onNodeWithTag("DROPDOWN_OPTION_$i").assertDoesNotExist()
        }


    }
}