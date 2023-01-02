package com.msharialsayari.musrofaty.ui_component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme

object TextComponent {

    @Composable
    fun HeaderText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        color:Color = MusrofatyTheme.colors.textHeaderColor,
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
        color:Color = MusrofatyTheme.colors.textBodyColor,
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
        color:Color = MusrofatyTheme.colors.textPlaceHolderColor,
        enableEllipsis:Boolean = false,
        text: String
    ) {

        if (enableEllipsis){
            Text(
                text = text,
                modifier = modifier,
                color = color,
                textAlign = alignment,
                fontSize = dimensionResource(id = R.dimen.text_small).value.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,)

        }else {
            Text(
                text = text,
                modifier = modifier,
                color = color,
                textAlign = alignment,
                fontSize = dimensionResource(id = R.dimen.text_small).value.sp,
                fontWeight = FontWeight.Normal,

                )
        }

    }


    @Composable
    fun ClickableText(
        modifier: Modifier = Modifier,
        alignment: TextAlign = TextAlign.Start,
        color:Color = MusrofatyTheme.colors.textClickableColor,
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