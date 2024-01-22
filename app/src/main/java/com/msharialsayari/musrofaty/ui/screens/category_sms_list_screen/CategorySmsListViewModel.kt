package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirestoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.patrykandpatrick.vico.core.extension.getFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class CategorySmsListViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val getSendersUseCase: GetSendersUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val postStoreToFirestoreUseCase: PostStoreToFirestoreUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context


) : ViewModel(){

    private val _uiState = MutableStateFlow(CategorySmsListUIState())
    val uiState: StateFlow<CategorySmsListUIState> = _uiState

    companion object{
        const val CATEGORY_ID_KEY = "categoryId"
        const val SMS_LIST_KEY = "smsList"
    }


    val categoryId: Int?
        get() {
            return savedStateHandle.get<Int>(CATEGORY_ID_KEY)
        }


    val smsList: List<SmsModel>
        get() {
            val jsonList = savedStateHandle.get<String>(SMS_LIST_KEY)
            val container = Gson().fromJson(jsonList, SmsContainer::class.java)
            val decodedList = container.list.map {
                it.senderModel?.senderIconUri =    URLDecoder.decode(it.senderModel?.senderIconUri, StandardCharsets.UTF_8.name())
                it.body=    URLDecoder.decode(it.body, StandardCharsets.UTF_8.name())
                it
            }
            return decodedList
        }



    init {
        getCategory()
        getSms()
        getSenders()
        getCategories()

    }

    private fun getCategories() {
        viewModelScope.launch {
            val categoriesResult = getCategoriesUseCase.invoke()
            _uiState.update {
                it.copy(
                    categories = categoriesResult
                )
            }
        }
    }

    fun addCategory(model: CategoryModel) {
        viewModelScope.launch {
            addCategoryUseCase.invoke(model)
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
                    isSelected = categoryId == value.id
                )
            )
        }

        return list
    }

    private fun getSms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update {
                it.copy(isLoading = false, smsList = smsList)
            }
        }
    }

    private fun getSenders(){
        viewModelScope.launch {
            val result = getSendersUseCase.invoke()
            _uiState.update {
                it.copy(senders = result)
            }
        }

    }

    fun onCategorySelected(item: SelectedItemModel) {
        viewModelScope.launch {
            val categoryId = item.id
            val storeName = _uiState.value.selectedSms?.storeAndCategoryModel?.store?.name
            val storeModel = storeName?.let { name -> StoreModel(name = name, categoryId = categoryId) }

            storeModel?.let {
                addOrUpdateStoreUseCase.invoke(it)
                postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
                _uiState.value.selectedSms?.let {smsModel->
                    updateSmsList(smsModel,storeModel, categoryId)
                }

            }
        }
    }

    private fun updateSmsList(smsModel: SmsModel,storeModel: StoreModel, newCategoryId:Int) {
        viewModelScope.launch {
            val selectedSms =getSmsById(smsModel)
            val newCategory = getCategoryUseCase.invoke(newCategoryId)
            if(selectedSms!= null && newCategory != null){
                selectedSms.storeAndCategoryModel = StoreAndCategoryModel(
                    store = storeModel,
                    category = newCategory
                )
                replaceSms(selectedSms)
            }
        }


    }

    private fun getSmsById(selectedSms:SmsModel): SmsModel? {
        return _uiState.value.smsList.find { it.id ==  selectedSms.id}
    }

    private fun replaceSms(selectedSms:SmsModel){
        val newList = _uiState.value.smsList.toMutableList().apply {
            removeIf {  it.id == selectedSms.id}
            selectedSms.apply {
                isSelected = !isSelected
            }
            add(selectedSms)
        }

        _uiState.update {
            it.copy(smsList = newList)
        }

    }


    fun onSmsCategoryClicked(item: SmsModel) {
        _uiState.update {
            it.copy(selectedSms = item)
        }
    }

    private fun getCategory() {
        viewModelScope.launch {
            if(categoryId != null){
                val category = getCategoryUseCase.invoke(categoryId!!)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        category = category ?: CategoryModel.getNoSelectedCategory(),
                    )

                }
            }
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


    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")
    }
    fun navigateUp(){
        navigator.navigateUp()
    }



}