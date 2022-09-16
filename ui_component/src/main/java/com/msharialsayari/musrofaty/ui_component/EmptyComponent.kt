package com.msharialsayari.musrofaty.ui_component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

object EmptyComponent {

    @Composable
    fun EmptyTextComponent(text:String= stringResource(id = R.string.no_result)){
        TextComponent.PlaceholderText(text = text)
    }
}