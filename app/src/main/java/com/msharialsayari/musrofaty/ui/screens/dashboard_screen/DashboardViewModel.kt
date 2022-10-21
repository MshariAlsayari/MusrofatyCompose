package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllSmsForSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllSmsForSendersUseCase: GetAllSmsForSendersUseCase,
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
):ViewModel(){


    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState



    fun getDate(){
        getFinancialStatistics()

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


    fun getFilterTimeOptions(context: Context, selectedItem:SelectedItemModel? = null ): List<SelectedItemModel> {
        val options = context.resources.getStringArray(R.array.filter_options)
        val list = mutableListOf<SelectedItemModel>()
        options.mapIndexed { index, value ->
            list.add(SelectedItemModel(
                id = index,
                value = value,
                isSelected = if (selectedItem != null) selectedItem.id == index else index == 0
            )
            )
        }

        return list

    }

    fun showStartDatePicker(){
        _uiState.update {
            it.copy(startDate = 0, endDate = 0, showStartDatePicker = true, showEndDatePicker = false)
        }
    }

    fun dismissAllDatePicker(){
        _uiState.update {
            it.copy(startDate = 0, endDate = 0, showStartDatePicker = false, showEndDatePicker = false)
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


     fun getFilterTimeOption(): DateUtils.FilterOption{
        return DateUtils.FilterOption.getFilterOption(_uiState.value.selectedFilterTimeOption?.id)
    }


    data class DashboardUiState(
        var isLoading: Boolean = false,
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