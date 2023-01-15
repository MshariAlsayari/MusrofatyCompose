package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.jobs.InitCategoriesJob
import com.msharialsayari.musrofaty.jobs.InitStoresJob
import com.msharialsayari.musrofaty.jobs.InsertSmsJob
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
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
    private val getCategoriesStatisticsUseCase: GetCategoriesStatisticsUseCase,
    private val loadAllSenderSmsUseCase: LoadAllSenderSmsUseCase,
    private val getSMSDashboardUseCase: GetSMSDashboardUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
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

    fun getData(){
        getSmsDashboard()
        getFinancialStatistics()
        getCategoriesStatistics()
    }

    fun loadSms(){
        viewModelScope.launch {
            _uiState.update { it.copy( isRefreshing = true) }
            loadAllSenderSmsUseCase.invoke()
            getSmsDashboard()
            getFinancialStatistics()
            getCategoriesStatistics()
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

    private fun getCategoriesStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCategoriesStatisticsSmsPageLoading = true) }
            val smsResult = getAllSmsForSendersUseCase.invoke(
                filterOption = getFilterTimeOption(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
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

    private fun getSmsDashboard(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSmsPageLoading = true)
            }

            val smsResult = getSMSDashboardUseCase.invoke(
                filterOption = getFilterTimeOption(),
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

    fun wrapSendersToSenderComponentModel(
        sms: SmsEntity,
        context: Context
    ): SmsComponentModel {

        return SmsComponentModel(
            id = sms.id,
            senderId= sms.senderId,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            body = sms.body,
            senderDisplayName = SenderModel.getDisplayName(context, getSenderById(sms.senderId)),
            senderCategory = ContentModel.getDisplayName(context, getSenderById(sms.senderId)?.content)

        )

    }

    fun getSenderById(senderId:Int): SenderModel? {
        return _uiState.value.senders.find { it.id == senderId }

    }

    fun favoriteSms(id:String , favorite:Boolean){
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }

    fun initJobs(context: Context){
        initInsertSmsJob(context)
    }


    private fun initCategoriesJob(context: Context){
        val initCategoriesWorker = OneTimeWorkRequestBuilder<InitCategoriesJob>().build()
        WorkManager.getInstance(context).enqueue(initCategoriesWorker)
    }

    private fun initStoresJob(context: Context){
        val initStoresWorker = OneTimeWorkRequestBuilder<InitStoresJob>().build()
        WorkManager.getInstance(context).enqueue(initStoresWorker)
    }

    private fun initInsertSmsJob(context: Context){
        val initStoresWorker = OneTimeWorkRequestBuilder<InsertSmsJob>().build()
        WorkManager.getInstance(context).enqueue(initStoresWorker)
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