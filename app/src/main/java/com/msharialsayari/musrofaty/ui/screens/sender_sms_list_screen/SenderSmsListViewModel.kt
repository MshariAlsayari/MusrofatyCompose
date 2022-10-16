package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.work.Data
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.jobs.GenerateExcelFileJob
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenderSmsListViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getAllSms: GetAllSmsUseCase,
    private val getFavoriteSmsUseCase: GetFavoriteSmsUseCase,
    private val getSmsBySenderIdUseCase: GetSmsBySenderIdUseCase,
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
    private val getCategoriesStatisticsUseCase: GetCategoriesStatisticsUseCase,
    private val getFiltersUseCase: GetFiltersUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(SenderSmsListUiState())
    val uiState: StateFlow<SenderSmsListUiState> = _uiState

    fun onFilterChanged(){
        val senderId = _uiState.value.sender?.id!!
        getAllSms(senderId)
        getFavoriteSms(senderId)
        getFinancialStatistics(senderId)
        getCategoriesStatistics(senderId)
        getAllSmsBySenderId(senderId)
    }




    fun getSender(senderId: Int){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val senderResult         = getSenderUseCase.invoke(senderId)
            if (senderResult !=null)
            getFilters(senderResult.id)
            _uiState.update {
                it.copy(
                    sender          = senderResult,
                    isLoading       = false )
            }
        }
    }

    private fun getFilters(senderId:Int){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val filtersResult = getFiltersUseCase.invoke(senderId)
            _uiState.update {
                it.copy(
                    filters = filtersResult,
                    isLoading = false
                )
            }
        }
    }


    fun getAllSms(senderId: Int){
        viewModelScope.launch {
            _uiState.update { it.copy(isAllSmsPageLoading = false) }
            val smsResult            = getAllSms.invoke(senderId, filterOption = getFilterTimeOption(), query = getFilterWord(), startDate = _uiState.value.startDate, endDate = _uiState.value.endDate,)
            _uiState.update {
                it.copy(
                    smsFlow         = smsResult,
                    isAllSmsPageLoading       = false )
            }
        }

    }

     fun getAllSmsBySenderId(senderId: Int){
        viewModelScope.launch {
            val smsResult            = getSmsBySenderIdUseCase.invoke(senderId, filterOption = getFilterTimeOption(),query = getFilterWord(), startDate = _uiState.value.startDate, endDate = _uiState.value.endDate)
            _uiState.update {
                it.copy(
                    allSmsFlow         = smsResult)
            }
        }

    }

    fun getFavoriteSms(senderId: Int){
        viewModelScope.launch {
            _uiState.update { it.copy(isFavoriteSmsPageLoading = false) }
            val smsResult            = getFavoriteSmsUseCase.invoke(senderId, filterOption = getFilterTimeOption(),query = getFilterWord(), startDate = _uiState.value.startDate, endDate = _uiState.value.endDate)
            _uiState.update {
                it.copy(
                    favoriteSmsFlow = smsResult,
                    isFavoriteSmsPageLoading       = false )
            }
        }

    }


    fun favoriteSms(id:String , favorite:Boolean){
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }

    fun getFinancialStatistics(senderId: Int){
        viewModelScope.launch {
            _uiState.update { it.copy(isFinancialStatisticsSmsPageLoading = true) }
            val smsResult  = getSmsBySenderIdUseCase.invoke(senderId, filterOption = getFilterTimeOption(), query = getFilterWord(), startDate = _uiState.value.startDate, endDate = _uiState.value.endDate)
            smsResult.collectLatest { list->
                val result = getFinancialStatisticsUseCase.invoke(list)
                _uiState.update { state ->
                    state.copy(
                        financialStatistics = result,
                        isFinancialStatisticsSmsPageLoading = false
                    )
                }

            }
        }
    }

    fun getCategoriesStatistics(senderId: Int){
        viewModelScope.launch {
            _uiState.update { it.copy(isCategoriesStatisticsSmsPageLoading = true) }
            val smsResult  = getSmsBySenderIdUseCase.invoke(senderId, filterOption = getFilterTimeOption(), query = getFilterWord(), startDate = _uiState.value.startDate, endDate = _uiState.value.endDate)
            smsResult.collectLatest { list->
                val result = getCategoriesStatisticsUseCase.invoke(list)
                _uiState.update { state ->
                    state.copy(
                        categoriesStatistics = result,
                        isCategoriesStatisticsSmsPageLoading = false
                    )
                }

            }
        }
    }




    fun wrapSendersToSenderComponentModel(
        sms: SmsEntity,
        context: Context
    ): SmsComponentModel {

        return SmsComponentModel(
            id = sms.id,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            body = sms.body,
            senderDisplayName = SenderModel.getDisplayName(context, _uiState.value.sender),
            senderCategory = ContentModel.getDisplayName(context, _uiState.value.sender?.content)

        )

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

    fun getFilterOptions(selectedItem:SelectedItemModel? = null ): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        _uiState.value.filters.mapIndexed { index, value ->
            list.add(SelectedItemModel(
                id = value.id,
                value = value.title,
                isSelected = selectedItem?.id == value.id
            )
            )
        }

        return list

    }

     fun getFilterTimeOption():DateUtils.FilterOption{
        return DateUtils.FilterOption.getFilterOption(_uiState.value.selectedFilterTimeOption?.id)
    }

    private fun getFilterWord(): String {
        return _uiState.value.filters.find { it.id ==  _uiState.value.selectedFilter?.id }?.words ?:""
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

    fun  getDataBuilder():Data{
        val builder = Data.Builder()
        builder.putInt(GenerateExcelFileJob.SENDER_ID, _uiState.value.sender?.id?:0)
        builder.putInt(GenerateExcelFileJob.FILTER_TIME_OPTION, uiState.value.selectedFilterTimeOption?.id?:0)
        builder.putString(GenerateExcelFileJob.FILTER_WORD, uiState.value.selectedFilter?.value?:"")
        builder.putLong(GenerateExcelFileJob.START_TIME, uiState.value.startDate)
        builder.putLong(GenerateExcelFileJob.END_TIME, uiState.value.endDate)
        return builder.build()
    }


    data class SenderSmsListUiState(
        var isLoading: Boolean = false,
        var isAllSmsPageLoading: Boolean = false,
        var isFavoriteSmsPageLoading: Boolean = false,
        var isFinancialStatisticsSmsPageLoading: Boolean = false,
        var isCategoriesStatisticsSmsPageLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        val sender: SenderModel? = null,
        var smsFlow: Flow<PagingData<SmsEntity>>? =null,
        var favoriteSmsFlow: Flow<PagingData<SmsEntity>>? =null,
        var allSmsFlow: Flow<List<SmsEntity>>? =null,
        var selectedFilterTimeOption:SelectedItemModel? = null,
        var selectedFilter:SelectedItemModel? = null,
        var filters: List<FilterAdvancedModel> = emptyList(),
        var financialStatistics: Map<String, FinancialStatistics> = emptyMap(),
        var categoriesStatistics: Map<Int, CategoryStatistics> = emptyMap(),
        var startDate: Long = 0,
        var endDate: Long = 0,
        var showStartDatePicker: Boolean = false,
        var showEndDatePicker: Boolean = false,
    )
}