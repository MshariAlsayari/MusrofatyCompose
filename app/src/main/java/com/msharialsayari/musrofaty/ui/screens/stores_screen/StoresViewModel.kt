package com.msharialsayari.musrofaty.ui.screens.stores_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllStoreWithCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoresViewModel @Inject constructor(
    private val getAllStoreWithCategoryUseCase: GetAllStoreWithCategoryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoresUiState())
    val uiState: StateFlow<StoresUiState> = _uiState

    init {
        getStores()
    }

    private fun getStores(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getAllStoreWithCategoryUseCase.invoke()
            _uiState.update {
                it.copy(isLoading = false,stores = result)
            }
        }
    }

    data class StoresUiState(
        var isLoading: Boolean = false,
       var stores: Flow<List<StoreWithCategory>>? = null
    )
}