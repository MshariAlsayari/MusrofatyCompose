package com.msharialsayari.musrofaty.ui.screens.content_screen

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel

data class ContentUIState(
        val content: ContentModel? = null,
        var valueAr: String = "",
        var valueEn: String = "",
        var valueArValidationModel: ValidationModel = ValidationModel(),
        var valueEnValidationModel:ValidationModel = ValidationModel(),
    )