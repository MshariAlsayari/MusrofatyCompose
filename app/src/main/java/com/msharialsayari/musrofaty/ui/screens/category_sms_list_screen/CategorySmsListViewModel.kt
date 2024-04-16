package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.google.gson.Gson
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChart
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChartModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesStatisticsChartUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryChartUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFiltersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.LoadSenderSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingPaginationAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirebaseUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateSenderIconUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomsheets.CategorySmsListBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.SenderSmsListBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsViewModel
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
    private val observingAllSmsUseCase: ObservingPaginationAllSmsUseCase,
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
    private val getCategoriesStatisticsChartUseCase: GetCategoryChartUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context,
) : ViewModel(){


    private val _uiState = MutableStateFlow(CategorySmsListUiState())
    val uiState: StateFlow<CategorySmsListUiState> = _uiState

    companion object{
        private val TAG = CategorySmsListViewModel::class.java.simpleName
    }



    init{
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

    fun getData() {
        getSmsListTabs()
        getChart()
    }

    private fun getChart() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    financialTabLoading = true,
                )
            }

            val startDate  = _uiState.value.startDate
            val endDate    = _uiState.value.endDate
            val timeOption = getFilterTimeOption()
            val category   = getSelectedCategory()

            val smsList = getAllSmsModelUseCase.invoke(
                filterOption = timeOption,
                startDate = startDate,
                endDate = endDate,
                categoryId = category
            )


            val groupSmsByCurrent = smsList.groupBy { it.currency }
            val charts = mutableListOf<CategoriesChart>()
            groupSmsByCurrent.forEach { (key, value) ->
                val result = getCategoriesStatisticsChartUseCase.invoke(key,timeOption,value)
                if (result.total > 0)
                    charts.add(result)
                Log.d(TAG, "chart() title:$key result: ${value.size}")

            }


            _uiState.update {
                it.copy(
                    financialTabLoading = false,
                    financialStatistics = charts
                )
            }

        }

    }

    private fun getSmsListTabs() {
        viewModelScope.launch {

            val allSmsTab = observingAllSmsUseCase(
                filterOption = getFilterTimeOption(),
                categoryId = getSelectedCategory(),
                isDeleted = null,
                isFavorite = null,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )

            val favoriteSmsTab = observingAllSmsUseCase(
                filterOption = getFilterTimeOption(),
                categoryId = getSelectedCategory(),
                isDeleted = null,
                isFavorite = true,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )


            val softDeletedSmsTab = observingAllSmsUseCase(
                filterOption = getFilterTimeOption(),
                categoryId = getSelectedCategory(),
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

    fun getFilterTimeOption(): DateUtils.FilterOption {
        return if (_uiState.value.selectedFilterTimeOption != null) {
            DateUtils.FilterOption.getFilterOptionOrDefault(_uiState.value.selectedFilterTimeOption?.id, default = DateUtils.FilterOption.MONTH)
        } else {
            _uiState.value.startDate = DateUtils.getSalaryDate()
            _uiState.value.endDate = DateUtils.getCurrentDate()
            val timeFilterOptionId = SharedPreferenceManager.getFilterTimePeriod(context)
            val timeFilterOption = DateUtils.FilterOption.getFilterOptionOrDefault(timeFilterOptionId, default = DateUtils.FilterOption.MONTH)
            _uiState.value.selectedFilterTimeOption = SelectedItemModel(id = timeFilterOptionId, value = context.getString(timeFilterOption.title), isSelected = true)
            timeFilterOption
        }
    }

    private fun getSelectedCategory():Int?{
        return if(_uiState.value.category.id == -1) null else _uiState.value.category.id
    }
    fun onTabSelected(tabIndex: Int) {
        _uiState.update {
            it.copy(selectedTabIndex = tabIndex)
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

    fun onDatePeriodsSelected(start: Long, end: Long){
        _uiState.update {
            it.copy(startDate = start,endDate = end)
        }
    }

    fun onCategorySelected(item: SelectedItemModel) {
        viewModelScope.launch {
            val categoryId = item.id
            val category = getCategoryUseCase.invoke(categoryId)

            category?.let {category->
                _uiState.update {
                    it.copy(category = category)
                }
                getData()
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

    fun navigateBack(){
        navigator.navigateUp()

    }

    fun navigateToSmsDetails(smsID: String) {
        navigator.navigate(Screen.SmsScreen.route + "/${smsID}")
    }

    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")
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