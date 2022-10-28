package com.msharialsayari.musrofaty.ui_component.date_picker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate


@Composable
internal fun CalendarDay(
    date: DateWrapper,
    onSelected: (LocalDate) -> Unit
) {

    var currentModifier = Modifier
        .aspectRatio(1F)
        .clip(CircleShape)

    if (!date.isCurrentMonth && date.showCurrentMonthOnly) {
        Box(modifier = currentModifier)
        return
    }

    currentModifier = when {
        date.isSelectedDay -> {
            currentModifier
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape
                )
        }
        date.isCurrentDay -> {
            currentModifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape
                )
        }
        else -> currentModifier
    }

    if (date.isInDateRange || (date.isCurrentMonth && date.showCurrentMonthOnly)) {
        currentModifier = currentModifier.clickable {
            onSelected(date.localDate)
        }
    }

    val textColor = when {
        !date.isInDateRange -> {
            MaterialTheme.colors.onSurface.copy(alpha = 0.38F)
        }
        date.isSelectedDay -> {
            MaterialTheme.colors.onPrimary
        }
        date.isCurrentDay -> {
            MaterialTheme.colors.primary
        }
        !date.isCurrentMonth -> {
            MaterialTheme.colors.primary.copy(alpha = 0.6F)
        }
        else ->  MaterialTheme.colors.onBackground
    }

    val text = "${date.localDate.dayOfMonth}"

    Box(
        modifier = currentModifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
        )
    }
}
