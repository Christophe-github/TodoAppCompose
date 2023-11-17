package com.example.todoappcompose.ui.common

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.time.LocalTime

fun showTimePickerDialog(
    context: Context,
    initialTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit
) {

    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onTimeChange(LocalTime.of(hourOfDay, minute))
        },
        initialTime.hour,
        initialTime.minute,
        false
    )
        .show()
}


@Composable
@Deprecated("CustomTimePicker does not work for api 21",
    replaceWith = ReplaceWith("showTimePickerDialog"))
fun CustomTimePicker(
    initialTime: LocalTime,
    modifier: Modifier = Modifier,
    onTimeSelected: (LocalTime) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var hour by remember { mutableStateOf(initialTime.hour) }
    var minute by remember { mutableStateOf(initialTime.minute) }


    fun hideDialog() {
        showDialog = false
    }

    TextButton(onClick = { showDialog = !showDialog }, modifier = modifier) {
        Text("Change".uppercase(), style = MaterialTheme.typography.button)
    }

    if (!showDialog)
        return

    Dialog(onDismissRequest = ::hideDialog) {
        Column(Modifier.background(MaterialTheme.colors.background)) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    TimePicker(context).apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            this.hour = hour
                            this.minute = minute
                        }
                    }
                },
                update = { view ->
                    view.setOnTimeChangedListener { _, newHourOfDay, newMinute ->
                        hour = newHourOfDay
                        minute = newMinute
                    }
                }
            )

            Row(modifier = Modifier.wrapContentSize()) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = ::hideDialog) {
                    Text("Cancel".uppercase())
                }
                TextButton(onClick = { onTimeSelected(LocalTime.of(hour, minute));hideDialog() }) {
                    Text("Ok".uppercase())
                }
            }
        }

    }

}

