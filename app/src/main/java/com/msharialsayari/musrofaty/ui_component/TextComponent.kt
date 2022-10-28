package com.msharialsayari.musrofaty.ui_component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.msharialsayari.musrofaty.R

object TextComponent {

    @Composable
    fun HeaderText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        color:Color = colorResource(id = R.color.text_header_color),
        text: String,

        ) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = alignment,
            color= color,
            fontSize = dimensionResource(id = R.dimen.text_big).value.sp,
            fontWeight = FontWeight.Bold
        )

    }


    @Composable
    fun BodyText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        color:Color = colorResource(id = R.color.text_body_color),
        text: String
    ) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = alignment,
            color= color,
            fontSize = dimensionResource(id = R.dimen.text_medium).value.sp
        )

    }


    @Composable
    fun PlaceholderText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        color:Color = colorResource(id = R.color.text_place_holder_color),
        text: String
    ) {
        Text(
            text = text,
            modifier = modifier,
            color= color,
            textAlign = alignment,
            fontSize = dimensionResource(id = R.dimen.text_small).value.sp,
            fontWeight = FontWeight.Normal
        )

    }


    @Composable
    fun ClickableText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        color:Color = colorResource(id = R.color.text_clickable_color),
        text: String
    ) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = alignment,
            color=color,
            fontSize = dimensionResource(id = R.dimen.text_small).value.sp,
            fontWeight = FontWeight.Normal
        )

    }
}