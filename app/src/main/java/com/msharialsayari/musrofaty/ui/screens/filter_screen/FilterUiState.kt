package com.msharialsayari.musrofaty.ui.screens.filter_screen

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel

data class FilterUiState(
        var senderId: Int = 0,
        var filterId: Int = 0,
        var title: String = "",
        var words: String = "",
        var isCreateNewFilter:Boolean = false,
        var titleValidationModel: ValidationModel = ValidationModel(),
        var wordValidationModel:ValidationModel? = null,
)