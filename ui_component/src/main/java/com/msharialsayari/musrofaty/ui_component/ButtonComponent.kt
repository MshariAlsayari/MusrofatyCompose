package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource

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
}
