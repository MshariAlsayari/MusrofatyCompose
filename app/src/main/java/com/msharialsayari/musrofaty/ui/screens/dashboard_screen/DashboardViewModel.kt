package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllSmsForSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.LoadAllSenderSmsUseCase
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllSmsForSendersUseCase: GetAllSmsForSendersUseCase,
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
    private val loadAllSenderSmsUseCase: LoadAllSenderSmsUseCase
):ViewModel(){


    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState



    fun getDate(){
        getFinancialStatistics()

    }

    fun loadSms(){
        viewModelScope.launch {
            _uiState.update { it.copy( isRefreshing = true) }
            loadAllSenderSmsUseCase.invoke()
            getFinancialStatistics()
            _uiState.update { state ->
                state.copy(
                    isRefreshing = false
                )
            }

        }
    }

    private fun getFinancialStatistics(){
        viewModelScope.launch {
            _uiState.update { it.copy(isFinancialStatisticsSmsPageLoading = true) }
            val smsResult  = getAllSmsForSendersUseCase.invoke( filterOption = getFilterTimeOption(), startDate = _uiState.value.startDate, endDate = _uiState.value.endDate)
            val result = getFinancialStatisticsUseCase.invoke(smsResult)
            _uiState.update { state ->
                state.copy(
                    financialStatistics = result,
                    isFinancialStatisticsSmsPageLoading = false
                )
            }

        }
    }




    fun showStartDatePicker(){
        _uiState.update {
            it.copy(startDate = 0, endDate = 0, showStartDatePicker = true, showEndDatePicker = false)
        }
    }

    fun dismissAllDatePicker(){
        _uiState.update {
            it.copy(showStartDatePicker = false, showEndDatePicker = false)
        }
    }

    fun onStartDateSelected(value:Long){
        _uiState.update {
            it.copy(startDate = value, showStartDatePicker = false, showEndDatePicker = true)
        }
    }

    fun onEndDateSelected(value:Long){
        _uiState.update {
            it.copy(endDate = value, showStartDatePicker = false, showEndDatePicker = false)
        }

    }

    fun onFilterTimeOptionSelected(item: SelectedItemModel){
        _uiState.update {
            it.copy(selectedFilterTimeOption = item)
        }
    }



    fun getFilterTimeOption(): DateUtils.FilterOption{
         return if (_uiState.value.selectedFilterTimeOption != null) {
             DateUtils.FilterOption.getFilterOption(_uiState.value.selectedFilterTimeOption?.id)
         }else{
             _uiState.value.startDate= DateUtils.getSalaryDate()
             _uiState.value.endDate= DateUtils.getCurrentDate()
             _uiState.value.selectedFilterTimeOption = SelectedItemModel(id = 5, value = "", isSelected = true)
             DateUtils.FilterOption.RANGE
         }

    }


    data class DashboardUiState(
        var isLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        var allSmsFlow: Flow<List<SmsEntity>>? =null,
        var selectedFilterTimeOption: SelectedItemModel? = null,
        var startDate: Long = 0,
        var endDate: Long = 0,
        var showStartDatePicker: Boolean = false,
        var showEndDatePicker: Boolean = false,
        var isFinancialStatisticsSmsPageLoading: Boolean = false,
        var financialStatistics: Map<String, FinancialStatistics> = emptyMap()
    )

}