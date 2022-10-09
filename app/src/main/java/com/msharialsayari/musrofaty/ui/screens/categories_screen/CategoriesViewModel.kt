package com.msharialsayari.musrofaty.ui.screens.categories_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStore
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryWithStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
private val getCategoriesUseCase: GetCategoriesUseCase,
private val getCategoryWithStoresUseCase: GetCategoryWithStoresUseCase,


) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState
    init {
        getAllCategories()
    }
    fun getData(categoryId: Int){
        _uiState.update {
            it.copy(categoryId = categoryId)
        }

        getAllCategories()
        getCategoryWithStores()

    }

     private fun getAllCategories(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getCategoriesUseCase.invoke()
            _uiState.update {
                it.copy(isLoading = false, categories = result)
            }
        }
    }

    private fun getCategoryWithStores(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getCategoryWithStoresUseCase.invoke(_uiState.value.categoryId)
             result?.collectLatest {category->
                _uiState.update {
                    it.copy(isLoading = false, categoryWithStores = result, arabicCategory = category.category?.valueAr?:"", englishCategory = category.category?.valueEn?:"")
                }
            }

        }
    }

    data class CategoriesUiState(
        var isLoading:Boolean = false,
        val categoryId:Int= 0,
        var categories: Flow<List<CategoryEntity>>? =null,
        var categoryWithStores:Flow<CategoryWithStore>? = null,
        var arabicCategory:String = "",
        var englishCategory:String = ""
    )
}