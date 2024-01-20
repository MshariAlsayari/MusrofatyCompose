package com.msharialsayari.musrofaty.ui.screens.sms_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetStoreAndCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirestoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
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
    private val postStoreToFirestoreUseCase: PostStoreToFirestoreUseCase,
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
                addOrUpdateStoreUseCase.invoke(it)
                postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
            }

            getData(_uiState.value.sms?.id!!)
        }
    }


    fun wrapSendersToSenderComponentModel(
        sms: SmsModel,
        context: Context
    ): SmsComponentModel {
        val store = _uiState.value.storeAndCategoryModel?.store?.name ?: ""
        var category = ""
        if (store.isNotEmpty()) {
            category =
                CategoryModel.getDisplayName(context, uiState.value.storeAndCategoryModel?.category)
            if (category.isEmpty())
                category = context.getString(R.string.common_no_category)
        }

        return SmsComponentModel(
            id = sms.id,
            senderId = sms.senderId,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            isDeleted = sms.isDeleted,
            body = sms.body,
            storeName = store,
            storeCategory = category,
            senderIcon = _uiState.value.sender?.senderIconUri ?: "",
            senderDisplayName = SenderModel.getDisplayName(context, _uiState.value.sender),
            senderCategory = ContentModel.getDisplayName(context, _uiState.value.sender?.content)

        )

    }

    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")
    }


    fun navigateUp(){
        navigator.navigateUp()
    }
}