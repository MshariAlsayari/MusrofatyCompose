package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.google.gson.Gson
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFiltersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.LoadSenderSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingPaginationAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirebaseUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateSenderIconUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomsheets.CategorySmsListBottomSheetType
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySmsListViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val observingPaginationAllSmsUseCase: ObservingPaginationAllSmsUseCase,
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
    private val getFiltersUseCase: GetFiltersUseCase,
    private val getAllSmsModelUseCase: GetSmsModelListUseCase,
    private val loadSenderSmsUseCase: LoadSenderSmsUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val updateSenderIconUseCase: UpdateSenderIconUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val postStoreToFirebaseUseCase: PostStoreToFirebaseUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAllSmsUseCase: GetAllSmsUseCase,
    private val getFilterUseCase: GetFilterUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context,
) : ViewModel(){


    private val _uiState = MutableStateFlow(CategorySmsListUiState(category = CategoryModel()))
    val uiState: StateFlow<CategorySmsListUiState> = _uiState

    companion object{
        const val CATEGORY_ID_KEY = "categoryId"
    }

    val categoryId: Int
        get() {
            return savedStateHandle.get<Int>(CATEGORY_ID_KEY)!!
        }

    init{
        initCategory()
        getCategories()
    }

    private fun initCategory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getCategoryUseCase.invoke(categoryId)
            if (result != null) {
                _uiState.update {
                    it.copy(
                        category = result,
                        title = CategoryModel.getDisplayName(context, result)
                    )
                }
                getData()
            }else{
                navigator.navigateUp()
            }

            _uiState.update {
                it.copy(
                    isLoading = false
                )
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

    fun getData() {
        getFinancialStatistics()
        getSmsListTabs()
        getSmsTotal()
    }

    fun getFinancialStatistics() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    financialTabLoading = true,
                )
            }

            val smsList = getAllSmsModel(isDeleted = false)
            val result = getFinancialStatisticsUseCase.invoke(smsList)
            _uiState.update { state ->
                state.copy(
                    financialTabLoading = false,
                    financialStatistics = result,
                )
            }


        }
    }

    fun onDatePeriodsSelected(start: Long, end: Long){
        _uiState.update {
            it.copy(startDate = start,endDate = end)
        }
    }

    fun updateBottomSheetType(type: CategorySmsListBottomSheetType?){
        _uiState.update {
            it.copy(
                bottomSheetType = type,
            )
        }
    }

    fun updateSelectedFilterTimePeriods(selectedItem: SelectedItemModel?){
        _uiState.update {
            it.copy(
                selectedFilterTimeOption = selectedItem,
            )
        }
    }

    private suspend fun getAllSmsModel(isDeleted: Boolean?=null): List<SmsModel> {
        return getAllSmsModelUseCase.invoke(
            categoryId = categoryId,
            filterOption = getFilterTimeOption(),
            isDeleted = isDeleted,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate
        )
    }

    private fun getSmsListTabs() {
        viewModelScope.launch {

            val allSmsTab : Flow<PagingData<SmsModel>> = observingPaginationAllSmsUseCase(
                categoryId =  categoryId,
                filterOption = getFilterTimeOption(),
                isDeleted = null,
                isFavorite = null,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )

            val favoriteSmsTab : Flow<PagingData<SmsModel>> = observingPaginationAllSmsUseCase(
                categoryId =  categoryId,
                filterOption = getFilterTimeOption(),
                isDeleted = null,
                isFavorite = true,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )


            val softDeletedSmsTab : Flow<PagingData<SmsModel>> = observingPaginationAllSmsUseCase(
                categoryId =  categoryId,
                filterOption = getFilterTimeOption(),
                isDeleted = true,
                isFavorite = null,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )

            _uiState.update { state ->
                state.copy(
                    allSmsList = allSmsTab,
                    favoriteSmsList = favoriteSmsTab,
                    softDeletedSmsList = softDeletedSmsTab
                )
            }
        }

    }

    private fun getSmsTotal() {
        viewModelScope.launch {
            val result = getAllSmsUseCase.invoke(
                senderId =  categoryId,
                filterOption = getFilterTimeOption(),
                isDeleted = null,
                isFavorite = null,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )
            _uiState.update {
                it.copy(
                    totalSms = result.size
                )
            }
        }
    }

    fun getFilterTimeOption(): DateUtils.FilterOption {
        return if (_uiState.value.selectedFilterTimeOption != null) {
            DateUtils.FilterOption.getFilterOptionOrDefault(_uiState.value.selectedFilterTimeOption?.id, default = DateUtils.FilterOption.MONTH)
        } else {
            _uiState.value.startDate = DateUtils.getSalaryDate()
            _uiState.value.endDate = DateUtils.getCurrentDate()
            val savedFilterOption = SharedPreferenceManager.getFilterTimePeriod(context)
            val timeFilterOptionId = if (savedFilterOption == DateUtils.FilterOption.ALL.id ) DateUtils.FilterOption.MONTH.id else savedFilterOption
            val timeFilterOption = DateUtils.FilterOption.getFilterOptionOrDefault(timeFilterOptionId)
            _uiState.value.selectedFilterTimeOption = SelectedItemModel(id = timeFilterOptionId, value = context.getString(timeFilterOption.title), isSelected = true)
            DateUtils.FilterOption.getFilterOptionOrDefault(timeFilterOptionId)
        }
    }
    fun onTabSelected(tabIndex: Int) {
        _uiState.update {
            it.copy(selectedTabIndex = tabIndex)
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

    fun navigateBack(){
        navigator.navigateUp()
    }

    fun navigateToSmsDetails(smsID: String) {
        navigator.navigate(Screen.SmsScreen.route + "/${smsID}")
    }

    fun navigateToSmsListScreen(model: SmsContainer,
                                categoryModel: CategoryModel?,
                                isCategoryRowClicked:Boolean,
                                isExpensesSmsRowClicked:Boolean,
                                context: Context) {
        val title = if (isCategoryRowClicked) {
            CategoryModel.getDisplayName(context, categoryModel)
        } else if (isExpensesSmsRowClicked) {
            context.getString(R.string.expenses_sms_type)
        } else {
            context.getString(R.string.incomes_sms_type)
        }

        val json = Gson().toJson(model)
        val routeArgument = "/${title}"+ "/${json}"
        navigator.navigate(Screen.SmsListScreen.route + routeArgument)
    }
}