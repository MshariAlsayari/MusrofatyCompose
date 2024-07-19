package com.msharialsayari.musrofaty.ui.screens.categories_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStores
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryWithStoresUseCase: GetCategoryWithStoresUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val updateStoreUseCase: UpdateStoreUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val postStoreToFirebaseUseCase: PostStoreToFirebaseUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context


) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState

    init {
        getAllCategories()
    }

    fun getData(categoryId: Int) {
        _uiState.update {
            it.copy(categoryId = categoryId)
        }

        getAllCategories()
        getCategoryWithStores()

    }

    private fun getAllCategories() {
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

    private fun getCategoryWithStores() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getCategoryWithStoresUseCase.invoke(_uiState.value.categoryId)
            val category = getCategoryUseCase.invoke(_uiState.value.categoryId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    categoryWithStores = result,
                    arabicCategory = category?.valueAr ?: "",
                    englishCategory = category?.valueEn ?: ""
                )

            }


        }
    }

    fun onSaveBtnClicked() {
        viewModelScope.launch {
            val model = CategoryModel(
                id = _uiState.value.categoryId,
                valueAr = _uiState.value.arabicCategory,
                valueEn = _uiState.value.englishCategory
            )
            updateCategoryUseCase.invoke(model)
        }
    }


    fun onDeleteBtnClicked() {
        viewModelScope.launch {
            val model = CategoryModel(
                id = _uiState.value.categoryId,
                valueAr = _uiState.value.arabicCategory,
                valueEn = _uiState.value.englishCategory
            )
            deleteCategoryUseCase.invoke(model)
        }
    }

    fun onDeleteStoreActionClicked(storeName:String) {
        viewModelScope.launch {
            val model = StoreModel(
                name = storeName,
            )
            updateStoreUseCase.invoke(model)
        }
    }

    fun validate(): Boolean {
        val arabicCategory = uiState.value.arabicCategory.trim()
        val englishCategory = uiState.value.englishCategory.trim()
        val arabicCategoryValidationModel = ValidationModel()
        val englishCategoryValidationModel = ValidationModel()
        if (arabicCategory.isEmpty()) {
            arabicCategoryValidationModel.isValid = false
            arabicCategoryValidationModel.errorMsg =
                context.getString(R.string.validation_field_mandatory)
        }

        if (englishCategory.isEmpty()) {
            englishCategoryValidationModel.isValid = false
            englishCategoryValidationModel.errorMsg =
                context.getString(R.string.validation_field_mandatory)
        }

        _uiState.update {
            it.copy(
                arabicCategoryValidationModel = arabicCategoryValidationModel,
                englishCategoryValidationModel = englishCategoryValidationModel
            )
        }

        return arabicCategoryValidationModel.isValid && englishCategoryValidationModel.isValid
    }

    fun onArabicCategoryChanged(value: String) {
        _uiState.update {
            it.copy(arabicCategory = value)
        }
    }

    fun onEnglishCategoryChanged(value: String) {
        _uiState.update {
            it.copy(englishCategory = value)
        }
    }

    fun addCategory(model: CategoryModel){
        viewModelScope.launch {
            addCategoryUseCase.invoke(model)
        }
    }

    fun getCategoryItems(context: Context, categories: List<CategoryEntity> ): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        categories.map { value ->
            list.add(
                SelectedItemModel(
                    id = value.id,
                    value = CategoryModel.getDisplayName(context,value),
                    isSelected = _uiState.value.categoryId == value.id
                )
            )
        }

        return list

    }

    fun onUpdateStoreCategory(storeName:String, newCategoryId: Int) {
        viewModelScope.launch {
            val model = StoreModel(
                name = storeName,
                categoryId = newCategoryId
            )
            updateStoreUseCase.invoke(model)
            postStoreToFirebaseUseCase.invoke(model.toStoreEntity())
        }
    }

    fun navigateUp(){
        navigator.navigateUp()
    }
}