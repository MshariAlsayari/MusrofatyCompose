package com.msharialsayari.musrofaty.ui_component.date_picker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FilterChip
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.utils.mirror

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetTime
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun CalendarMonthYearSelector(
    pagerDate: LocalDate,
    onChipClicked: () -> Unit,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
) {


    val pagerMonthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            content = {
                Text(
                    pagerMonthFormat.format(
                        Date.from(pagerDate.atTime(OffsetTime.now()).toInstant())
                    )
                )
            },
            selected = false,
            border = null,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, "ArrowDropDown")
            },
            onClick = onChipClicked,
        )
        Spacer(modifier = Modifier.weight(1F))
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.KeyboardArrowLeft, "ChevronLeft", Modifier.mirror())
        }
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.KeyboardArrowRight, "ChevronRight", Modifier.mirror())
        }
    }
}
