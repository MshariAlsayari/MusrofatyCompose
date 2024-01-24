package com.msharialsayari.musrofaty.ui.screens.stores_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import kotlinx.coroutines.flow.Flow

data class StoresUiState(
        var isLoading: Boolean = false,
        var stores: Flow<List<StoreWithCategory>>? = null,
        var categories: Flow<List<CategoryEntity>>? = null,
        var selectedStore: StoreWithCategory? = null,
    )