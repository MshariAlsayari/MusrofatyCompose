package com.msharialsayari.musrofaty.ui.screens.categories_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStores
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel
import kotlinx.coroutines.flow.Flow

data class CategoriesUiState(
        var isLoading: Boolean = false,
        var btnClicked: Boolean = false,
        val categoryId: Int = 0,
        var categories: Flow<List<CategoryEntity>>? = null,
        var categoryWithStores: Flow<CategoryWithStores?>? = null,
        var arabicCategory: String = "",
        var englishCategory: String = "",
        var arabicCategoryValidationModel: ValidationModel = ValidationModel(),
        var englishCategoryValidationModel: ValidationModel = ValidationModel(),
    )