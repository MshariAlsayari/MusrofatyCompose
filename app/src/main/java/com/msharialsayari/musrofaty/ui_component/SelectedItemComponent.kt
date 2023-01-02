package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun StringSelectedItemComponent(modifier: Modifier = Modifier, model: SelectedItemModel, canUnSelect:Boolean = false,canDoubleSelect:Boolean = true, onSelect:(Boolean)->Unit, onLongPress:(SelectedItemModel)->Unit = {} ) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (canUnSelect){
                        onSelect(!model.isSelected)
                    }else{

                        if (canDoubleSelect){
                            onSelect(true)
                        }else{
                            if (!model.isSelected){
                                onSelect(true)
                            }
                        }

                    }
                },
                onLongClick = {
                              onLongPress(model)
                },
            ),
        text = { TextComponent.BodyText(text = model.value) },
        secondaryText = { TextComponent.PlaceholderText(text = model.description) },
        trailing = {
            if (model.isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colors.secondary)
            }

        }
    )

}


data class SelectedItemModel(
    var id: Int,
    var value: String,
    var description: String="",
    var isSelected: Boolean = false,
)