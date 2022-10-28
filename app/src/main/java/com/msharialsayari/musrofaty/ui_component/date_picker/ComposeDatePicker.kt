package com.msharialsayari.musrofaty.ui_component.date_picker



import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import java.time.LocalDate

@Composable
fun ComposeDatePicker(
    startDate: LocalDate = LocalDate.now(),
    minDate: LocalDate = LocalDate.MIN,
    maxDate: LocalDate = LocalDate.now(),
    title:String="",
    onDone: (millis: LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedDate = remember { mutableStateOf(startDate) }

    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDone(selectedDate.value)
            }) {
                Text(stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        text = {
            CalendarContent(
                startDate = startDate,
                minDate = minDate,
                maxDate = maxDate,
                title = title,
                onSelected = {
                    selectedDate.value = it
                }
            )
        }
    )
}