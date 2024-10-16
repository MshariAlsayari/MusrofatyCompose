package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R

object ButtonComponent {

    @Composable
    fun ActionButton(modifier: Modifier=Modifier,@StringRes text: Int, color:Color?=null,onClick: () -> Unit = {}) {
        Button(
            onClick  = onClick,
            shape    = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = color?.let { color} ?:MaterialTheme.colors.primary

            ),
            modifier = modifier
                .height(dimensionResource(R.dimen.btn_height50)),
            ) {
            Text(text = stringResource(id = text), color = MaterialTheme.colors.onPrimary)
        }
    }

    @Composable
    fun OutlineButton(@StringRes text: Int, onClick: () -> Unit = {}) {
        OutlinedButton(
            onClick  = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
            shape = RoundedCornerShape(50),
            modifier = Modifier) {
            Text(text = stringResource(id = text))
        }
    }

    @Composable
    fun FloatingButton(modifier: Modifier=Modifier, icon: ImageVector = Icons.Filled.Add,  onClick: () -> Unit = {}) {
        FloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = Color.White
        ) {
            Icon(icon, "")
        }
    }

    @Composable
    fun FloatingButton(modifier: Modifier=Modifier, firstIcon: ImageVector, secondIcon: ImageVector , isFirstPosition:Boolean =  true, onClick: () -> Unit = {}) {
        FloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = Color.White
        ) {


            AnimatedVisibility(
                visible = isFirstPosition,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(firstIcon, "")
            }
            AnimatedVisibility(
                visible = !isFirstPosition,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(secondIcon, "")
            }
        }
    }
}
