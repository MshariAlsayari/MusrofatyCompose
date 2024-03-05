package com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets

import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel

@Composable
fun AddFilterBottomSheetCompose(
    item: FilterWordModel? = null,
    onActionClicked: (FilterWordModel?, String) -> Unit
) {
    val model = TextFieldBottomSheetModel(
        title = if (item != null) R.string.common_change else R.string.common_add,
        label = R.string.common_word,
        textFieldValue = item?.word ?: "",
        buttonText = if (item != null) R.string.common_change else R.string.common_add,
        onActionButtonClicked = { value ->
            onActionClicked(item, value)
        })

    BottomSheetComponent.TextFieldBottomSheetComponent(model = model)

}