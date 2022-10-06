package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource

object BottomSheetComponent {

    @Composable
    fun TextFieldBottomSheetComponent(modifier: Modifier= Modifier, model:TextFieldBottomSheetModel){
        val text  =  remember { mutableStateOf(model.textFieldValue) }
        text.value = text.value
        Column(modifier = modifier) {
            TextComponent.HeaderText(text = stringResource(id = model.title) , modifier = modifier.padding(dimensionResource(id = R.dimen.default_margin16)))
            DividerComponent.HorizontalDividerComponent()
            TextFieldComponent.BoarderTextFieldComponent(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.default_margin16)),
                textValue = text.value,
                errorMsg = "",
                onValueChanged = {
                    text.value= it
                }
            )
            ButtonComponent.ActionButton(text = model.buttonText, onClick = {
                model.onActionButtonClicked(text.value)
                text.value = ""
            })

        }

    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SelectedItemListBottomSheetComponent(modifier: Modifier= Modifier,
                                             @StringRes title:Int,
                                             @StringRes description:Int?=null,
                                             list: List<SelectedItemModel>,
                                             trailIcon : (@Composable ()->Unit)? = null,
                                             canUnSelect:Boolean = false,
                                             onSelectItem:(SelectedItemModel)->Unit,
                                             onLongPress:(SelectedItemModel)->Unit = {}
    ){

        Column(modifier = modifier) {
            if (trailIcon == null){
                TextComponent.HeaderText(
                    text = stringResource(id = title),
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16))
                )
                description?.let {
                    TextComponent.PlaceholderText(
                        text = stringResource(id = description),
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16))
                    )
                }
        }else{
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.default_margin16)), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column (verticalArrangement = Arrangement.Center){
                        TextComponent.HeaderText(text = stringResource(id = title) )
                        description?.let {
                            TextComponent.PlaceholderText(
                                text = stringResource(id = description),
                                modifier = Modifier
                            )
                        }
                    }

                    trailIcon()
                }
            }
            DividerComponent.HorizontalDividerComponent()
            list.forEach {item->
                StringSelectedItemComponent(model = item,
                    canUnSelect = canUnSelect,
                    onSelect = {
                    item.isSelected = it
                    onSelectItem(item)
                },
                    onLongPress = {
                        onLongPress(it)
                    }
                )
            }


        }

    }

}

data class TextFieldBottomSheetModel(
    @StringRes var title:Int,
    var textFieldValue:String="",
    @StringRes var buttonText:Int,
    var onActionButtonClicked:(String)->Unit,

)