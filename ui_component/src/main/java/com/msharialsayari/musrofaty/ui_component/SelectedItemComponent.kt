package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StringSelectedItemComponent(modifier: Modifier = Modifier, model: SelectedItemModel, onSelect:(Boolean)->Unit) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (!model.isSelected){
                    onSelect(!model.isSelected)
                }

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
)