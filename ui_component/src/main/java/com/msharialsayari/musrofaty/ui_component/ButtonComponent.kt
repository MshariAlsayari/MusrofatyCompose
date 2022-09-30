package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

object ButtonComponent {

    @Composable
    fun ActionButton(@StringRes text: Int, onClick: () -> Unit = {}) {
        Button(
            onClick  = onClick,
            shape    = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.btn_height60)),
            ) {
            Text(text = stringResource(id = text), color = MaterialTheme.colors.onBackground)
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
}
