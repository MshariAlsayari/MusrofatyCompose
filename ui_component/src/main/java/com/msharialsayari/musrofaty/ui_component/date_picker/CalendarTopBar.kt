package com.msharialsayari.musrofaty.ui_component.date_picker


import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.time.LocalDate
import java.time.OffsetTime
import java.util.*

@Composable
internal fun CalendarTopBar(selectedDate: LocalDate, title:String="") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {


        val dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT)

        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )


        Text(
            text = dateFormatter.format(
                Date.from(selectedDate.atTime(OffsetTime.now()).toInstant())
            ),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onBackground
        )
    }
}
