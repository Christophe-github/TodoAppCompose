package com.example.todoappcompose.ui.common

import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

/**
 * A custom material DropDown since the official one has not yet been released.
 * @param selected The current selected value from the drop down
 * @param options The values to choose from the drop down
 * @param onOptionSelect The callback when an option is selected, the index of the option is given
 *
 */
@ExperimentalMaterialApi
@Composable
fun MyDropDown(label : String,
               selected : String,
               options : List<String>,
               onOptionSelect : (Int) -> Unit,
               modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selected,
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            })

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = MaterialTheme.colors.background)
        ) {
            options.forEachIndexed{ index, option ->
                DropdownMenuItem(
                    modifier = Modifier.testTag("DROPDOWN_OPTION_$index"),
                    onClick = {
                        expanded = false
                        onOptionSelect(index) }) {
                    Text(text = option)
                }
            }
        }
    }
}




@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun MyDropDownPreview() {
    MyDropDown("Sort by","Option one", listOf("Option one","Option two","Option three"), {} )
}