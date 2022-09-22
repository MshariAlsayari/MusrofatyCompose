package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StringSelectedItemComponent(modifier: Modifier = Modifier, model: SelectedItemModel) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                model.onSelect(!model.isSelected)
            },
        text = { TextComponent.BodyText(text = model.value) },
        trailing = {
            if (model.isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
            }

        }
    )

}


data class SelectedItemModel(
    var id: Int,
    var value: String,
    var isSelected: Boolean = false,
    var onSelect: (Boolean) -> Unit,
)