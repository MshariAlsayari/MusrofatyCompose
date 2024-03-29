package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

object TextFieldComponent {


    @Composable
    fun BoarderTextFieldComponent(
        modifier: Modifier = Modifier,
         textValue: String? = "",
         onValueChanged:(String) ->Unit,
         keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
         keyboardActions: KeyboardActions = KeyboardActions(),
         readOnly: Boolean = false,
         errorMsg:String ="",
        @StringRes  placeholder:Int?=null,
        @StringRes  label:Int?=null,
        isSingleLine: Boolean = false,
    ) {


        Column(modifier = modifier ) {


            TextField(
                modifier = modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    placeholder?.let {
                        TextComponent.PlaceholderText(text = stringResource(id = placeholder))
                    }

                },

                label = {
                    label?.let {
                        TextComponent.PlaceholderText(text = stringResource(id = label))
                    }

                },
                value = textValue?:"",
                readOnly = readOnly,
                isError = errorMsg.isNotEmpty(),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = isSingleLine,
                onValueChange = {
                    onValueChanged(it)
                }
            )

            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

        }

    }
}

data class TextFieldModel(
    var textValue: String,
    val onValueChanged:(String) ->Unit,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val keyboardActions: KeyboardActions = KeyboardActions(),
    val readOnly: Boolean = false,
    @StringRes val placeholder:Int?=null,
    @StringRes val label:Int?=null,
)