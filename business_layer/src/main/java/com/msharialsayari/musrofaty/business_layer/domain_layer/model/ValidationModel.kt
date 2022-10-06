package com.msharialsayari.musrofaty.business_layer.domain_layer.model

data class ValidationModel(
    var isValid  :Boolean = true,
    var errorMsg :String = "",
)