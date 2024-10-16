package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.notEmpty

object DialogComponent {

    @Composable
    fun MusrofatyDialog(
        title: String = "",
        message: String = "",
        positiveBtnText: String? = null,
        negativeBtnText: String? = null,
        cancelable: Boolean = false,
        onClickPositiveBtn: () -> Unit = {},
        onClickNegativeBtn: () -> Unit = {}
    ) {

        Dialog(onDismissRequest = { },
            DialogProperties(
                dismissOnBackPress = cancelable,
                dismissOnClickOutside = cancelable
            )) {
            Card(
                modifier = Modifier.defaultMinSize(minHeight = 150.dp, minWidth = 300.dp)
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16), Alignment.Top)
                ) {

                    TextComponent.HeaderText(
                        text = title
                    )

                    TextComponent.BodyText(
                        text = message
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        if (negativeBtnText?.isNotEmpty() == true)
                            TextButton(onClick = onClickNegativeBtn) {
                                Text(text = negativeBtnText)
                            }

                        if (positiveBtnText?.isNotEmpty() == true)
                            TextButton(onClick = onClickPositiveBtn) {
                                Text(text = positiveBtnText)
                            }

                    }

                }
            }
        }

    }


    @Composable
    fun AddCategoryDialog(
        onClickPositiveBtn: (String, String) -> Unit,
        onClickNegativeBtn: () -> Unit
    ) {
        val context = LocalContext.current
        val arabicField = remember { mutableStateOf("") }
        val englishField = remember { mutableStateOf("") }

        val errorArabicField = remember { mutableStateOf("") }
        val errorEnglishField = remember { mutableStateOf("") }

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(0.dp)
        ) {
            Column {
                TextComponent.HeaderText(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16)),
                    text = stringResource(id = R.string.category)
                )
                DividerComponent.HorizontalDividerComponent()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.default_margin16)),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextFieldComponent.BoarderTextFieldComponent(
                        textValue = arabicField.value,
                        errorMsg = errorArabicField.value,
                        label = R.string.arabic,
                        onValueChanged = {
                            arabicField.value = it
                        }
                    )


                    TextFieldComponent.BoarderTextFieldComponent(
                        textValue = englishField.value,
                        errorMsg = errorEnglishField.value,
                        label = R.string.english,
                        onValueChanged = {
                            englishField.value = it
                        }
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.default_margin16),
                            end = dimensionResource(id = R.dimen.default_margin16),
                            bottom = dimensionResource(id = R.dimen.default_margin16)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ButtonComponent.ActionButton(
                        modifier = Modifier.weight(1f),
                        text = R.string.add,
                        onClick = {
                            if (!arabicField.value.notEmpty()) {
                                errorArabicField.value =
                                    context.getString(R.string.validation_field_mandatory)
                            } else {
                                errorArabicField.value = ""
                            }

                            if (!englishField.value.notEmpty()) {
                                errorEnglishField.value =
                                    context.getString(R.string.validation_field_mandatory)
                            } else {
                                errorEnglishField.value = ""
                            }

                            if (arabicField.value.notEmpty() && englishField.value.notEmpty()) {
                                onClickPositiveBtn(arabicField.value, englishField.value)
                            }
                        }
                    )


                    ButtonComponent.ActionButton(
                        modifier = Modifier.weight(1f),
                        text = R.string.cancel,
                        color = MusrofatyTheme.colors.deleteActionColor,
                        onClick = {
                            onClickNegativeBtn()
                        }
                    )

                }

            }
        }

    }

    @Composable
    fun MessageDialog(
        @StringRes message: Int,
        onDismiss: () -> Unit,
    ) {

        Dialog(onDismissRequest = onDismiss) {
            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextComponent.BodyText(
                        text = stringResource(id = message),
                        alignment = TextAlign.Center
                    )
                    TextComponent.ClickableText(modifier = Modifier.clickable {
                        onDismiss()
                    }, text = stringResource(id = R.string.common_ok), alignment = TextAlign.Center)
                }

            }

        }


    }

    @Composable
    fun LoadingDialog(
        @StringRes message: Int
    ) {

        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {

            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            ) {

                Row(
                    Modifier
                        .padding(dimensionResource(id = R.dimen.default_margin16)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16)),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    ProgressBar.CircleProgressBar()
                    TextComponent.PlaceholderText(text = stringResource(id = message))

                }

            }
        }

    }

    @Composable
    fun TimeOptionDialog(
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

        Dialog(onDismissRequest = {}) {
            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            ) {
                BottomSheetComponent.SelectedItemListBottomSheetComponent(
                    title = R.string.common_filter_options,
                    list = list,
                    onSelectItem = {
                        onFilterSelected(it)
                    }
                )
            }


        }
    }

    @Composable
    fun ConfirmationDialog(
        title: String = "",
        message: String = "",
        cancelable: Boolean = false,
        positiveBtnText: String? = stringResource(id = R.string.common_yes),
        negativeBtnText: String? = stringResource(id = R.string.common_cancel),
        onClickPositiveBtn: () -> Unit = {},
        onClickNegativeBtn: () -> Unit = {}
    ) {

        MusrofatyDialog(
            title = title,
            message = message,
            cancelable=cancelable,
            positiveBtnText = positiveBtnText,
            negativeBtnText = negativeBtnText,
            onClickPositiveBtn = onClickPositiveBtn,
            onClickNegativeBtn = onClickNegativeBtn
        )

    }
}