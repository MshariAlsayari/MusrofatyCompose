package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.notEmpty

object BottomSheetComponent {

    @Composable
    fun TextFieldBottomSheetComponent(
        modifier: Modifier = Modifier,
        model: TextFieldBottomSheetModel,
        errorMsg:String  = ""
    ) {
        val context = LocalContext.current
        val text = remember{ mutableStateOf(model.textFieldValue) }
        val error = remember { mutableStateOf(errorMsg) }

        Column(modifier = modifier) {
            TextComponent.HeaderText(
                text = stringResource(id = model.title),
                modifier = modifier.padding(
                    start = dimensionResource(id = R.dimen.default_margin16),
                    end = dimensionResource(id = R.dimen.default_margin16),
                    top = dimensionResource(id = R.dimen.default_margin16)
                )
            )
            model.description?.let { description ->
                TextComponent.PlaceholderText(
                    text = stringResource(id = description),
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16))
                )
            }
            DividerComponent.HorizontalDividerComponent(
                Modifier.padding(
                    vertical = dimensionResource(
                        id = R.dimen.default_margin16
                    )
                ),
            )
            TextFieldComponent.BoarderTextFieldComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.default_margin16),
                        end = dimensionResource(id = R.dimen.default_margin16),
                        bottom = dimensionResource(id = R.dimen.default_margin16)
                    ),
                textValue = text.value,
                errorMsg = error.value,
                label= model.label,
                isSingleLine = model.isSingleLine,
                keyboardActions = model.keyboardActions,
                keyboardOptions = model.keyboardOptions,
                onValueChanged = {
                    text.value = it
                }
            )
            ButtonComponent.ActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = model.buttonText, onClick = {
                    if (text.value.notEmpty()) {
                        model.onActionButtonClicked(text.value)
                    } else {
                        error.value = context.getString(R.string.validation_field_mandatory)
                    }
                })

        }

    }


    @Composable
    fun SelectedItemListBottomSheetComponent(
        modifier: Modifier = Modifier,
        @StringRes title: Int,
        @StringRes description: Int? = null,
        emptyListText: String = stringResource(id = R.string.no_result),
        list: List<SelectedItemModel>,
        trailIcon: (@Composable () -> Unit)? = null,
        canUnSelect: Boolean = false,
        onSelectItem: (SelectedItemModel) -> Unit,
        onLongPress: (SelectedItemModel) -> Unit = {}
    ) {

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            if (trailIcon == null) {
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
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.default_margin16)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        TextComponent.HeaderText(text = stringResource(id = title))
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

            if (list.isNotEmpty()) {
                list.forEach { item ->
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
            } else {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyComponent.EmptyTextComponent(text = emptyListText)
                }
            }


        }

    }

    @OptIn(ExperimentalMaterialApi::class)
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


    @Composable
    fun TimeOptionsBottomSheet(
        @StringRes  title: Int? = null,
        selectedItem: SelectedItemModel? = null,
        ignoreFilterOption: DateUtils.FilterOption? = null,
        startDate: Long = 0,
        endDate: Long = 0,
        onFilterSelected: (SelectedItemModel) -> Unit
    ) {
        val context = LocalContext.current

        val options =
            if (ignoreFilterOption == null || ignoreFilterOption.id == selectedItem?.id) DateUtils.FilterOption.entries
            else DateUtils.FilterOption.entries.filter { ignoreFilterOption.id != it.id }

        val list = mutableListOf<SelectedItemModel>()
        options.map { item ->
            list.add(
                SelectedItemModel(
                    id = item.id,
                    value = context.getString(item.title),
                    description = if (DateUtils.FilterOption.isRangeDateSelected(selectedItem?.id) && item.id == 5) DateUtils.formattedRangeDate(
                        startDate,
                        endDate
                    ) else context.getString(item.subtitle),
                    isSelected = if (selectedItem != null) selectedItem.id == item.id else item.id == 0
                )
            )
        }

        SelectedItemListBottomSheetComponent(
            title = title ?: R.string.common_filter_options,
            list = list,
            onSelectItem = {
                onFilterSelected(it)
            }
        )


    }


}

data class TextFieldBottomSheetModel(
    @StringRes var title: Int,
    @StringRes var label: Int? = null,
    @StringRes var description: Int? = null,
    var textFieldValue: String = "",
    @StringRes var buttonText: Int,
    var onActionButtonClicked: (String) -> Unit,
    var isSingleLine: Boolean = true,
    var keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    var keyboardActions: KeyboardActions = KeyboardActions(),

    )