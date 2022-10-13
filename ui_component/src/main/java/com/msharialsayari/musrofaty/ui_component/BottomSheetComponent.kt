package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.utils.notEmpty

object BottomSheetComponent {

    @Composable
    fun TextFieldBottomSheetComponent(modifier: Modifier= Modifier, model:TextFieldBottomSheetModel){
        val context  = LocalContext.current
        val text  =  remember { mutableStateOf(model.textFieldValue) }
        val error = remember { mutableStateOf("") }
        text.value = text.value
        Column(modifier = modifier) {
            TextComponent.HeaderText(text = stringResource(id = model.title) , modifier = modifier.padding(dimensionResource(id = R.dimen.default_margin16)))
            DividerComponent.HorizontalDividerComponent()
            TextFieldComponent.BoarderTextFieldComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.default_margin16)),
                textValue = text.value,
                errorMsg = error.value,
                onValueChanged = {
                    text.value= it
                }
            )
            ButtonComponent.ActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = model.buttonText, onClick = {
                    if (text.value.notEmpty()) {
                        model.onActionButtonClicked(text.value)
                        text.value = ""
                        error.value = ""
                    }else{
                        error.value = context.getString(R.string.validation_field_mandatory)
                    }
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

        Column(modifier = modifier
            .verticalScroll(rememberScrollState())) {
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

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    suspend fun handleVisibilityOfBottomSheet(sheetState: ModalBottomSheetState, show: Boolean) {

        if (show) {
            sheetState.show()
        } else {
            sheetState.hide()
        }

    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    suspend fun handleVisibilityOfBottomSheet(sheetState: BottomSheetScaffoldState, show: Boolean) {

        if (show) {
            sheetState.bottomSheetState.expand()
        } else {
            sheetState.bottomSheetState.collapse()
        }

    }


}

data class TextFieldBottomSheetModel(
    @StringRes var title:Int,
    var textFieldValue:String="",
    @StringRes var buttonText:Int,
    var onActionButtonClicked:(String)->Unit,

)