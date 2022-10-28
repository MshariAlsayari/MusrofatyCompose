package com.msharialsayari.musrofaty.ui_component.date_picker

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.MaterialTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun CalendarYear(
    year: Int,
    isSelectedYear: Boolean,
    isCurrentYear: Boolean,
    setSelectedYear: (Int) -> Unit
) {


    if (isSelectedYear) {
        Button(
            onClick = {
                setSelectedYear(year)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text("$year", maxLines = 1)
        }
    } else if (isCurrentYear) {
        OutlinedButton(
            onClick = {
                setSelectedYear(year)
            },
            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.primary)
        ) {
            Text("$year", maxLines = 1)
        }
    } else {
        TextButton(
            onClick = { setSelectedYear(year) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text("$year", maxLines = 1)
        }
    }
}
