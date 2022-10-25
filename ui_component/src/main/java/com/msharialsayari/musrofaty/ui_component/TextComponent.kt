package com.msharialsayari.musrofaty.ui_component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object TextComponent {

    @Composable
    fun HeaderText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        text: String
    ) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = alignment,
            fontSize = dimensionResource(id = R.dimen.text_big).value.sp,
            fontWeight = FontWeight.Bold
        )

    }


    @Composable
    fun BodyText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        text: String
    ) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = alignment,
            fontSize = dimensionResource(id = R.dimen.text_medium).value.sp
        )

    }


    @Composable
    fun PlaceholderText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        text: String
    ) {
        Text(
            text = text,
            modifier = modifier,
            color= colorResource(id = R.color.light_gray),
            textAlign = alignment,
            fontSize = dimensionResource(id = R.dimen.text_small).value.sp,
            fontWeight = FontWeight.Normal
        )

    }


    @Composable
    fun ClickableText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        text: String
    ) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = alignment,
            color= MaterialTheme.colors.secondary,
            fontSize = dimensionResource(id = R.dimen.text_small).value.sp,
            fontWeight = FontWeight.Normal
        )

    }
}