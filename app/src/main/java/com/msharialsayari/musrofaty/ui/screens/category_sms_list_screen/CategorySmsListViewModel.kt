package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirestoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val postStoreToFirestoreUseCase: PostStoreToFirestoreUseCase,
    private val observingAllSmsUseCase: ObservingAllSmsUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context


) : ViewModel(){

    private val _uiState = MutableStateFlow(CategorySmsListUIState())
    val uiState: StateFlow<CategorySmsListUIState> = _uiState
    private var observeSmsJob: Job? = null

    companion object{
        const val FILTER_OPTION_KEY = "filterOption"
        const val QUERY_KEY = "query"
        const val START_DATE_KEY = "startDate"
        const val END_DATE_KEY = "endDate"
        const val CATEGORY_ID_KEY = "categoryId"
    }


    val filterOption: DateUtils.FilterOption
        get() {
            val filterId = savedStateHandle.get<Int>(FILTER_OPTION_KEY)
            return DateUtils.FilterOption.getFilterOptionOrDefault(filterId)
        }

    val query: String
        get() {
            val q = savedStateHandle.get<String>(QUERY_KEY)
            return if(q.isNullOrEmpty() || q.equals("null", ignoreCase = true)) "" else q
        }
    val startDate: Long
        get() {
            return savedStateHandle.get<Long>(START_DATE_KEY) ?: 0
        }

    val endDate: Long
        get() {
            return savedStateHandle.get<Long>(END_DATE_KEY)?: 0
        }

    val categoryId: Int?
        get() {
            val id = savedStateHandle.get<Int>(CATEGORY_ID_KEY)
            return if(id==null || id == -1) null else savedStateHandle.get<Int>(CATEGORY_ID_KEY)
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

        observingAllSmsUseCase.invoke(
                filterOption = filterOption,
                startDate = startDate,
                endDate = endDate,
                query = query,
                categoryId = categoryId
            ).collect{list->
                _uiState.update {
                    it.copy(smsList = list)
                }

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
                    isSelected = categoryId == value.id
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
                addOrUpdateStoreUseCase.invoke(it)
                postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
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