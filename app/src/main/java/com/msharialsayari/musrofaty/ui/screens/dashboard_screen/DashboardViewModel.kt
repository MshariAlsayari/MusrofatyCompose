package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.LoadAllSenderSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingPaginationAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.dialogs.DashboardDialogType
import com.msharialsayari.musrofaty.ui_component.CategoryStatisticsModel
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
    private val getCategoriesStatisticsUseCase: GetCategoriesStatisticsUseCase,
    private val loadAllSenderSmsUseCase: LoadAllSenderSmsUseCase,
    private val observingPaginationAllSmsUseCase: ObservingPaginationAllSmsUseCase,
    private val getAllSmsModelUseCase: GetSmsModelListUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val getSendersUseCase: GetSendersUseCase,
    private val navigator: AppNavigator
) : ViewModel() {


    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState


    init {
        getSenders()
    }

    private fun getSenders() {
        viewModelScope.launch {
            val result = getSendersUseCase.invoke()
            _uiState.update { it.copy(senders = result) }
        }
    }

    fun getData(context: Context) {
        getSmsDashboard(context)
        getFinancialStatistics(context)
        getCategoriesStatistics(context)
    }

    fun loadSms(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadAllSenderSmsUseCase.invoke()
            getSmsDashboard(context)
            getFinancialStatistics(context)
            getCategoriesStatistics(context)
            _uiState.update { state ->
                state.copy(
                    isRefreshing = false
                )
            }

        }
    }

    private suspend fun getAllSmsModel(context: Context): List<SmsModel> {
        return getAllSmsModelUseCase.invoke(
            filterOption = getFilterTimeOption(context),
            isDeleted = false,
            query = _uiState.value.query,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate
        )
    }

    private fun getFinancialStatistics(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFinancialStatisticsSmsPageLoading = true) }
            val smsResult = getAllSmsModel(context)
            val result = getFinancialStatisticsUseCase.invoke(smsResult)
            _uiState.update { state ->
                state.copy(
                    financialStatistics = result,
                    isFinancialStatisticsSmsPageLoading = false
                )
            }

        }
    }

    private fun getCategoriesStatistics(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCategoriesStatisticsSmsPageLoading = true) }
            val smsList = getAllSmsModel(context)
            val groupSmsByCurrent = smsList.groupBy { it.currency }
            val categories = mutableListOf<CategoryContainerStatistics>()
            groupSmsByCurrent.forEach { (key, value) ->
                val result = getCategoriesStatisticsUseCase.invoke(key, value)
                if (result.total > 0)
                    categories.add(result)
            }
            _uiState.update { state ->
                state.copy(
                    categoriesStatistics = categories,
                    isCategoriesStatisticsSmsPageLoading = false
                )
            }


        }
    }

    private fun getSmsDashboard(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSmsPageLoading = true)
            }

            val smsResult = observingPaginationAllSmsUseCase.invoke(
                filterOption = getFilterTimeOption(context),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
                query = _uiState.value.query
            )

            _uiState.update {
                it.copy(isSmsPageLoading = false, smsFlow = smsResult)
            }

        }

    }




    fun onFilterTimeOptionSelected(item: SelectedItemModel) {
        _uiState.update {
            it.copy(selectedFilterTimeOption = item)
        }
    }

    fun updateDialogType(dialogType: DashboardDialogType?) {
        _uiState.update {
            it.copy(dashboardDialogType = dialogType)
        }
    }

    fun onDatePeriodsSelected(start: Long, end: Long){
        _uiState.update {
            it.copy(startDate = start,endDate = end)
        }
    }

    fun onQueryChanged(value: String) {
        _uiState.update {
            it.copy(query = value)
        }

    }


    fun getFilterTimeOption(context: Context): DateUtils.FilterOption {
        return if (_uiState.value.selectedFilterTimeOption != null) {
            DateUtils.FilterOption.getFilterOptionOrDefault(_uiState.value.selectedFilterTimeOption?.id, default = DateUtils.FilterOption.MONTH)
        } else {
            _uiState.value.startDate = DateUtils.getSalaryDate()
            _uiState.value.endDate = DateUtils.getCurrentDate()
            val savedFilterOption = SharedPreferenceManager.getFilterTimePeriod(context)
            val timeFilterOptionId = if (savedFilterOption == DateUtils.FilterOption.ALL.id ) DateUtils.FilterOption.MONTH.id else savedFilterOption
            _uiState.value.selectedFilterTimeOption = SelectedItemModel(id = timeFilterOptionId, value = "", isSelected = true)
            DateUtils.FilterOption.getFilterOptionOrDefault(timeFilterOptionId)
        }

    }


    fun favoriteSms(id: String, favorite: Boolean) {
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }


    fun softDelete(context: Context, id: String, delete: Boolean) {
        viewModelScope.launch {
            softDeleteSMsUseCase.invoke(id, delete)
            getFinancialStatistics(context)
            getCategoriesStatistics(context)
        }
    }

    fun navigateToSmsDetails(smsId: String) {
        navigator.navigate(Screen.SmsScreen.route + "/${smsId}")
    }

    fun navigateToSenderSmsList(senderId: Int) {
        navigator.navigate(Screen.SenderSmsListScreen.route + "/${senderId}")
    }

    fun navigateToCategorySmsListScreen(model: CategoryStatisticsModel) {
        val categoryId = model.storeAndCategoryModel?.category?.id
        val ids=model.smsList.map { it.id }
        val smsContainer = SmsContainer(ids)
        val json = Gson().toJson(smsContainer)
        val routeArgument = "/${categoryId}"+ "/${json}"
        navigator.navigate(Screen.CategorySmsListScreen.route + routeArgument)
    }


}