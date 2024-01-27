package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingSmsListByIdsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirebaseUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomSheet.CategoryBottomSheetType
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SortedByAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySmsListViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val postStoreToFirebaseUseCase: PostStoreToFirebaseUseCase,
    private val observingAllSmsUseCase: ObservingSmsListByIdsUseCase,
    private val navigator: AppNavigator,
) : ViewModel(){

    private val _uiState = MutableStateFlow(CategorySmsListUIState())
    val uiState: StateFlow<CategorySmsListUIState> = _uiState
    private var observeSmsJob: Job? = null

    companion object{
        const val CATEGORY_ID_KEY = "categoryId"
        const val SMS_IDS_KEY = "ids"
    }

    val categoryId: Int?
        get() {
            val id = savedStateHandle.get<Int>(CATEGORY_ID_KEY)
            return if(id==null || id == -1) null else savedStateHandle.get<Int>(CATEGORY_ID_KEY)
        }


    private val ids: List<String>
        get() {
            val json = savedStateHandle.get<String>(SMS_IDS_KEY)
            val model = Gson().fromJson(json, SmsContainer::class.java)
            return model.ids
        }




    init {
        startObserveChat(true)
        getCategory()
        getCategories()
     
    }
    fun startObserveChat(force: Boolean = false) {
        if (force) {
            observeSmsJob?.cancelChildren()
            observeSmsJob = viewModelScope.launch {
                getSms()
            }
        } else if (observeSmsJob?.isActive == false || observeSmsJob == null) {
            observeSmsJob = viewModelScope.launch {
                getSms()
            }
        }
    }

    fun stopObserveChat() {
        observeSmsJob?.cancel()
    }
    

    
    private fun getSms() {
        viewModelScope.launch {

        observingAllSmsUseCase.invoke(ids).collect{list->
              updateList(getSortedList(list))
            }
        }

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
                    isSelected = _uiState.value.selectedSms?.storeAndCategoryModel?.category?.id == value.id
                )
            )
        }

        return list
    }



    fun onCategorySelected(item: SelectedItemModel) {
        viewModelScope.launch {
            val categoryId = item.id
            val storeName = _uiState.value.selectedSms?.storeAndCategoryModel?.store?.name
            val storeModel = storeName?.let { name -> StoreModel(name = name, categoryId = categoryId) }

            storeModel?.let {
                if(item.isSelected){
                    addOrUpdateStoreUseCase.invoke(it)
                    postStoreToFirebaseUseCase.invoke(storeModel.toStoreEntity())
                }else{
                    storeModel.categoryId = -1
                    addOrUpdateStoreUseCase.invoke(storeModel)
                }

                getSms()
            }
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
                        category = category ?: CategoryModel.getCategory(),
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
    fun updateSelectedSortByAmount(item:SortedByAmount){
        _uiState.update {
            it.copy(
                selectedSortedByAmount = item,
            )

        }
    }

    fun updateSelectedBottomSheet(type:CategoryBottomSheetType?){
        _uiState.update {
            it.copy(
                bottomSheetType = type,
            )
        }
    }


    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")
    }
    fun navigateUp(){
        navigator.navigateUp()
    }

    fun getSortedList(list: List<SmsModel>): List<SmsModel> {
        val amount = _uiState.value.selectedSortedByAmount
        return if (amount == SortedByAmount.HIGHEST)
            list.sortedByDescending { it.amount }
        else
            list.sortedBy { it.amount }
    }

    fun updateList(list: List<SmsModel>){
        _uiState.update {
            it.copy(smsList = list)
        }
    }
}