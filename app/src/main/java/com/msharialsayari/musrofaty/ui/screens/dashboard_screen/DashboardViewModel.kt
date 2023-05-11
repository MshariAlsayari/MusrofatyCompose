package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
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
    private val getCategoriesStatisticsUseCase: GetCategoriesStatisticsUseCase,
    private val loadAllSenderSmsUseCase: LoadAllSenderSmsUseCase,
    private val getSMSDashboardUseCase: GetSMSDashboardUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val getSendersUseCase: GetSendersUseCase,
):ViewModel(){


    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState


    init {
        getSenders()
    }



    private fun getSenders() {
        viewModelScope.launch {
            val result = getSendersUseCase.invoke()
            _uiState.update { it.copy( senders = result) }
        }
    }

    fun getData(context: Context) {
        getSmsDashboard(context)
        getFinancialStatistics(context)
        getCategoriesStatistics(context)
    }

    fun loadSms(context: Context){
        viewModelScope.launch {
            _uiState.update { it.copy( isRefreshing = true) }
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

    private fun getFinancialStatistics(context: Context){
        viewModelScope.launch {
            _uiState.update { it.copy(isFinancialStatisticsSmsPageLoading = true) }
            val smsResult  = getAllSmsForSendersUseCase.invoke(filterOption = getFilterTimeOption(context), startDate = _uiState.value.startDate, endDate = _uiState.value.endDate, query = _uiState.value.query)
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
            val smsResult = getAllSmsForSendersUseCase.invoke(
                filterOption = getFilterTimeOption(context),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
                query = _uiState.value.query
            )
            val result = getCategoriesStatisticsUseCase.invoke(smsResult)
            _uiState.update { state ->
                state.copy(
                    categoriesStatistics = result,
                    isCategoriesStatisticsSmsPageLoading = false
                )
            }


        }
    }

    private fun getSmsDashboard(context: Context){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSmsPageLoading = true)
            }

            val smsResult = getSMSDashboardUseCase.invoke(
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




    fun showStartDatePicker(){
        _uiState.update {
            it.copy(startDate = 0, endDate = 0, showStartDatePicker = true, showEndDatePicker = false)
        }
    }

    fun dismissAllDatePicker(){
        _uiState.update {
            it.copy(showStartDatePicker = false, showEndDatePicker = false, showFilterTimeOptionDialog = false)
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

    fun onDateRangeClicked(){
        _uiState.update {
            it.copy(showFilterTimeOptionDialog = !_uiState.value.showFilterTimeOptionDialog)
        }
    }

    fun onQueryChanged(value:String){
        _uiState.update {
            it.copy(query = value)
        }

    }



    fun getFilterTimeOption(context: Context): DateUtils.FilterOption{
         return if (_uiState.value.selectedFilterTimeOption != null) {
             DateUtils.FilterOption.getFilterOption(_uiState.value.selectedFilterTimeOption?.id)
         }else{
             _uiState.value.startDate= DateUtils.getSalaryDate()
             _uiState.value.endDate= DateUtils.getCurrentDate()
             val timeFilterOptionId = SharedPreferenceManager.getFilterTimePeriod(context)
             _uiState.value.selectedFilterTimeOption = SelectedItemModel(id = timeFilterOptionId, value = "", isSelected = true)
              DateUtils.FilterOption.getFilterOption(timeFilterOptionId)
         }

    }

    fun wrapSendersToSenderComponentModel(
        sms: SmsEntity,
        context: Context
    ): SmsComponentModel {

        return SmsComponentModel(
            id = sms.id,
            senderId= sms.senderId,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            isDeleted = sms.isDeleted,
            body = sms.body,
            senderDisplayName = SenderModel.getDisplayName(context, getSenderById(sms.senderId)),
            senderCategory = ContentModel.getDisplayName(context, getSenderById(sms.senderId)?.content)

        )

    }

    private fun getSenderById(senderId:Int): SenderModel? {
        return _uiState.value.senders.find { it.id == senderId }

    }

    fun favoriteSms(id:String , favorite:Boolean){
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }


    fun softDelete(context: Context,id:String , delete:Boolean){
        viewModelScope.launch {
            softDeleteSMsUseCase.invoke(id, delete)
            getFinancialStatistics(context)
            getCategoriesStatistics(context)
        }
    }


    data class DashboardUiState(
        var isLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        var isCategoriesStatisticsSmsPageLoading: Boolean = false,
        var selectedFilterTimeOption: SelectedItemModel? = null,
        var startDate: Long = 0,
        var endDate: Long = 0,
        var showStartDatePicker: Boolean = false,
        var showEndDatePicker: Boolean = false,
        var showFilterTimeOptionDialog: Boolean = false,
        var isFinancialStatisticsSmsPageLoading: Boolean = false,
        var financialStatistics: Map<String, FinancialStatistics> = emptyMap(),
        var categoriesStatistics: Map<Int, CategoryStatistics> = emptyMap(),
        var smsFlow: Flow<PagingData<SmsEntity>>? =null,
        var isSmsPageLoading: Boolean = false,
        var query:String="",
        var senders:List<SenderModel> = listOf()
    )

}