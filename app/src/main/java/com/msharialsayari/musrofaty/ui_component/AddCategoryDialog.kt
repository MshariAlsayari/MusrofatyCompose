package com.msharialsayari.musrofaty.ui_component

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel

@Composable
fun AddCategoryDialog(onAdd: (CategoryModel) -> Unit, onDismiss: () -> Unit) {

    Dialog(onDismissRequest = onDismiss) {

        DialogComponent.AddCategoryDialog(
            onClickPositiveBtn = { ar, en ->
                onAdd( CategoryModel(
                    valueEn = en,
                    valueAr = ar,
                ))
              

                onDismiss()

            },
            onClickNegativeBtn = onDismiss
        )

    }

}