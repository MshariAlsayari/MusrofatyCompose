package com.msharialsayari.musrofaty.ui.screens.stores_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllStoreWithCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.utils.notEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoresViewModel @Inject constructor(
    private val getAllStoreWithCategoryUseCase: GetAllStoreWithCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoresUiState())
    val uiState: StateFlow<StoresUiState> = _uiState

    init {
        getStores()
        getCategories()
    }

     fun getStores(storeName:String =""){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getAllStoreWithCategoryUseCase.invoke(storeName)
            _uiState.update {
                it.copy(isLoading = false,stores = result)
            }
        }
    }


    private fun getCategories(){
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

    fun getCategoryDisplayName(categoryId: Int, categories: List<CategoryEntity>):String{
        val entity = categories.find { it.id ==  categoryId}
        val displayName = CategoryModel.getDisplayName(context, entity)
        return if (displayName.notEmpty()){
            displayName
        }else{
            context.getString(R.string.common_no_category)
        }

    }

    fun navigateToStoreSmsListScreen(id:String){
        navigator.navigate(Screen.StoreSmsListScreen.route + "/${id}")
    }

    fun navigateUp(){
        navigator.navigateUp()
    }



}