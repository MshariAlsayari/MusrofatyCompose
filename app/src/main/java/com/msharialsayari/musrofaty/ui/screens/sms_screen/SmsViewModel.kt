package com.msharialsayari.musrofaty.ui.screens.sms_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetStoreAndCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirebaseUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsViewModel @Inject constructor(
    private val getSmsUseCase: GetSmsUseCase,
    private val getSenderUseCase: GetSenderUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getStoreAndCategoryUseCase: GetStoreAndCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val postStoreToFirebaseUseCase: PostStoreToFirebaseUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SmsUiState())
    val uiState: StateFlow<SmsUiState> = _uiState

    fun getData(id: String) {
        viewModelScope.launch {
            val smsResult = getSmsUseCase.invoke(id)
            smsResult?.let {
                val senderResult = getSenderUseCase.invoke(smsResult.senderId)
                val storeAndCategoryResult = getStoreAndCategoryUseCase.invoke(smsResult.storeName)
                val categoriesResult = getCategoriesUseCase.invoke()
                initSelectedItem(storeAndCategoryResult)
                _uiState.update {
                    it.copy(
                        sms = smsResult,
                        sender = senderResult,
                        storeAndCategoryModel = storeAndCategoryResult,
                        categories = categoriesResult
                    )
                }
            }

        }

    }

    private fun initSelectedItem(storeAndCategoryResult: StoreAndCategoryModel) {
        if (storeAndCategoryResult.category != null) {
            val category = storeAndCategoryResult.category
            if (category != null) {
                _uiState.update {
                    it.copy(
                        selectedCategory = SelectedItemModel(
                            id = category.id,
                            value = CategoryModel.getDisplayName(context = context, category),
                            isSelected = true
                        ),
                    )
                }
            }
        }
    }


    fun getCategoryItems(
        context: Context,
        categories: List<CategoryEntity>
    ): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        categories.map { value ->
            list.add(
                SelectedItemModel(
                    id = value.id,
                    value = CategoryModel.getDisplayName(context, value),
                    isSelected = _uiState.value.selectedCategory?.id == value.id
                )
            )
        }

        return list

    }

    fun addCategory(model: CategoryModel) {
        viewModelScope.launch {
            addCategoryUseCase.invoke(model)
        }
    }

    fun favoriteSms(id: String, favorite: Boolean) {
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }

    fun softDelete(id: String, delete: Boolean) {
        viewModelScope.launch {
            softDeleteSMsUseCase.invoke(id, delete)
        }
    }

    fun onCategorySelected(item: SelectedItemModel) {
        viewModelScope.launch {
            val categoryId = item.id
            val storeName = _uiState.value.storeAndCategoryModel?.store?.name
            val storeModel = storeName?.let { name -> StoreModel(name = name, categoryId = categoryId) }
            storeModel?.let {

                if (item.isSelected) {
                    addOrUpdateStoreUseCase.invoke(it)
                    getData(_uiState.value.sms?.id!!)
                    postStoreToFirebaseUseCase.invoke(storeModel.toStoreEntity())
                } else {
                    storeModel.categoryId = -1
                    addOrUpdateStoreUseCase.invoke(storeModel)
                    getData(_uiState.value.sms?.id!!)
                }
            }
        }
    }


    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")
    }


    fun navigateUp(){
        navigator.navigateUp()
    }
}