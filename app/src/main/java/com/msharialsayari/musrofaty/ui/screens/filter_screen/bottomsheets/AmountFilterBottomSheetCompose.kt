package com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel

@Composable
fun AmountFilterBottomSheetCompose(
    item: FilterAmountModel? = null,
    onActionClicked: (FilterAmountModel?, String) -> Unit
) {

    val context = LocalContext.current
    var isValidAmount by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var errorMsg = ""

    LaunchedEffect(key1 = isValidAmount){
      isValidAmount?.let {
          errorMsg = if (it){
              ""
          }else{
              context.getString(R.string.validation_field_amount)
          }
      }

    }
    val model = TextFieldBottomSheetModel(
        title = if (item != null) R.string.common_change else R.string.common_add,
        label = R.string.common_amount,
        textFieldValue = item?.amount ?: "",
        buttonText = if (item != null) R.string.common_change else R.string.common_add,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        onActionButtonClicked = { value ->
            isValidAmount = value.toDoubleOrNull() != null && value.last() != '.'
            if(isValidAmount == true){
                onActionClicked(item, value)
            }
        })
    BottomSheetComponent.TextFieldBottomSheetComponent(model = model, errorMsg = errorMsg)
}
