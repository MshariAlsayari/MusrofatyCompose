package com.msharialsayari.musrofaty.ui.screens.categories_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
private val getCategoriesUseCase: GetCategoriesUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState
    init {
        getAllCategories()
    }

    private fun getAllCategories(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getCategoriesUseCase.invoke()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    data class CategoriesUiState(
        val isLoading:Boolean = false,
        var categories: Flow<List<CategoryEntity>>? =null,
    )
}