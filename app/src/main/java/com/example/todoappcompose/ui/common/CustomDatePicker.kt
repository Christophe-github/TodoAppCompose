package com.example.todoappcompose.ui.common

import android.app.DatePickerDialog
import android.content.Context
import android.widget.CalendarView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.todoappcompose.R
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.ZoneOffset


fun showDatePickerDialog(
    context: Context,
    initialDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {

    //month start at 1 in LocalDate, but not in DatePickerDialog so we
    //need to adjust
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateChange(LocalDate.of(year, month + 1, dayOfMonth))
        },
        initialDate.year,
        initialDate.monthValue - 1,
        initialDate.dayOfMonth
    )
        .show()
}


@Composable
@Deprecated("DatePickerDialog already exists",
    replaceWith = ReplaceWith("showDatePickerDialog"))
fun CustomDatePicker(
    initialDate: LocalDate,
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit
) {


    var showDialog by remember { mutableStateOf(false) }
    var year by remember { mutableStateOf(initialDate.year) }
    var month by remember { mutableStateOf(initialDate.monthValue) }
    var dayOfMonth by remember { mutableStateOf(initialDate.dayOfMonth) }


    fun hideDialog() {
        showDialog = false
    }


    TextButton(onClick = { showDialog = true }, modifier = modifier) {
        Text("Change".uppercase())
    }

    if (!showDialog)
        return

    Dialog(onDismissRequest = ::hideDialog) {
        Column(Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.background)
        ) {
            AndroidView(
                //Setting a fixed height is mandatory for older android versions, for example on API 21
                modifier = Modifier.fillMaxWidth().height(300.dp),
                factory = { context -> CalendarView(context) },
                update = { view ->
                    val init = initialDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                    view.date = init
                    view.setOnDateChangeListener { _, newYear, newMonth, newDayOfMonth ->
                        year = newYear
                        //Month start at zero for this listener, but not days
                        month = newMonth + 1
                        dayOfMonth = newDayOfMonth
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = ::hideDialog) {
                    Text("Cancel".uppercase())
                }
                TextButton(onClick = {
                    onDateSelected(
                        LocalDate.of(
                            year,
                            month,
                            dayOfMonth
                        )
                    )
                    hideDialog()
                }) {
                    Text("Ok".uppercase())
                }
            }

        }
    }
}
