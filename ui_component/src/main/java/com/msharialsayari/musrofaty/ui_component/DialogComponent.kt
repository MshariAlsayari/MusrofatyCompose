package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.msharialsayari.musrofaty.utils.notEmpty

object DialogComponent {

    @Composable
    fun MusrofatyDialog(
        title: String = "",
        message: String = "",
        positiveBtnText: String? = null,
        negativeBtnText: String? = null,
        onClickPositiveBtn: () -> Unit = {},
        onClickNegativeBtn: () -> Unit = {}
    ) {

        Dialog(onDismissRequest = { }) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
            ) {
                Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16))) {

                    TextComponent.HeaderText(
                        text = title
                    )

                    TextComponent.BodyText(
                        modifier = Modifier.weight(1f),
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
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextComponent.HeaderText(text = stringResource(id = R.string.category))
            DividerComponent.HorizontalDividerComponent()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextFieldComponent.BoarderTextFieldComponent(
                    textValue = arabicField.value,
                    errorMsg = errorArabicField.value,
                    onValueChanged = {
                        arabicField.value = it
                    }
                )


                TextFieldComponent.BoarderTextFieldComponent(
                    textValue = englishField.value,
                    errorMsg = errorEnglishField.value,
                    onValueChanged = {
                        englishField.value = it
                    }
                )

            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonComponent.ActionButton(
                    text = R.string.add,
                    onClick = {
                        if (!arabicField.value.notEmpty()) {
                            errorArabicField.value = context.getString(R.string.validation_field_mandatory)
                        } else {
                            errorArabicField.value = ""
                        }

                        if (!englishField.value.notEmpty()) {
                            errorEnglishField.value = context.getString(R.string.validation_field_mandatory)
                        }else{
                            errorEnglishField.value = ""
                        }

                        if (arabicField.value.notEmpty() && englishField.value.notEmpty()){
                            onClickPositiveBtn(arabicField.value, englishField.value)
                        }
                    }
                )


                ButtonComponent.ActionButton(
                    text = R.string.cancel,
                    color = R.color.deletAction,
                    onClick = {
                        onClickNegativeBtn()
                    }
                )

            }

        }

    }
}