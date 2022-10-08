package com.msharialsayari.musrofaty.ui.screens.sms_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.*
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsViewModel @Inject constructor(
    private val getSmsUseCase: GetSmsUseCase,
    private val getSenderUseCase: GetSenderUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getStoreAndCategoryUseCase: GetStoreAndCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase
):ViewModel() {

    private val _uiState = MutableStateFlow(SmsUiState())
    val uiState: StateFlow<SmsUiState> = _uiState

    fun getData(id:String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val smsResult = getSmsUseCase.invoke(id)
            val senderResult = getSenderUseCase.invoke(smsResult.senderId)
            val storeAndCategoryResult = getStoreAndCategoryUseCase.invoke(smsResult.storeName)
            val categoriesResult = getCategoriesUseCase.invoke()
            _uiState.update {
                it.copy(isLoading = false, sms = smsResult, sender = senderResult, storeAndCategoryModel = storeAndCategoryResult, categories = categoriesResult)
            }
        }

    }


     fun getCategoryItems(context: Context, categories: List<CategoryEntity> ): List<SelectedItemModel> {
         val list = mutableListOf<SelectedItemModel>()
             categories.map { value ->
                 list.add(
                     SelectedItemModel(
                         id = value.id,
                         value = CategoryModel.getDisplayName(context,value),
                         isSelected = _uiState.value.selectedCategory?.id == value.id
                     )
                 )
             }

         return list

    }

    fun favoriteSms(id:String , favorite:Boolean){
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }

    fun onCategoryChanged(){
        viewModelScope.launch {
            val categoryId = _uiState.value.selectedCategory?.id ?: 0
            val storeName  = _uiState.value.storeAndCategoryModel?.store?.storeName
            val storeModel = storeName?.let { name -> StoreModel(storeName = name, categoryId = categoryId) }
            storeModel?.let {
                addOrUpdateStoreUseCase.invoke(it)
            }

            getData(_uiState.value.sms?.id!!)
        }
    }


    fun wrapSendersToSenderComponentModel(
        sms: SmsModel,
        context: Context
    ): SmsComponentModel {
        val store = _uiState.value.storeAndCategoryModel?.store?.storeName ?: ""
        var category = ""
        if ( store.isNotEmpty()){
            category = CategoryModel.getDisplayName(context, uiState.value.storeAndCategoryModel?.category)
            if (category.isEmpty())
            category = context.getString(R.string.common_no_category)
        }

        return SmsComponentModel(
            id = sms.id,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            body = sms.body,
            storeName=store,
            storeCategory= category,
            senderDisplayName = SenderModel.getDisplayName(context, _uiState.value.sender),
            senderCategory = ContentModel.getDisplayName(context, _uiState.value.sender?.content)

        )

    }

    data class SmsUiState (
        var isLoading: Boolean = false,
        var sms :SmsModel? = null,
        var sender :SenderModel? = null,
        var storeAndCategoryModel: StoreAndCategoryModel? = null,
        var selectedCategory: SelectedItemModel? = null,
        var categories: Flow<List<CategoryEntity>>? = null,
    )
}