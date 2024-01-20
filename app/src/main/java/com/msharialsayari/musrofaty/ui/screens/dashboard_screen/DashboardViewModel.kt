package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
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
    private val ObservingPaginationAllSmsUseCase: ObservingPaginationAllSmsUseCase,
    private val getAllSmsUseCase: GetSmsListUseCase,
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

    private suspend fun getAllSms(context: Context): List<SmsEntity> {
        return getAllSmsUseCase.invoke(
            filterOption = getFilterTimeOption(context),
            isDeleted = false,
            query = _uiState.value.query,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate
        )
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
            val smsResult = getAllSms(context)
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

            val smsResult = ObservingPaginationAllSmsUseCase.invoke(
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


    fun showStartDatePicker() {
        _uiState.update {
            it.copy(
                startDate = 0,
                endDate = 0,
                showStartDatePicker = true,
                showEndDatePicker = false
            )
        }
    }

    fun dismissAllDatePicker() {
        _uiState.update {
            it.copy(
                showStartDatePicker = false,
                showEndDatePicker = false,
                showFilterTimeOptionDialog = false
            )
        }
    }

    fun onStartDateSelected(value: Long) {
        _uiState.update {
            it.copy(startDate = value, showStartDatePicker = false, showEndDatePicker = true)
        }
    }

    fun onEndDateSelected(value: Long) {
        _uiState.update {
            it.copy(endDate = value, showStartDatePicker = false, showEndDatePicker = false)
        }

    }

    fun onFilterTimeOptionSelected(item: SelectedItemModel) {
        _uiState.update {
            it.copy(selectedFilterTimeOption = item)
        }
    }

    fun onDateRangeClicked() {
        _uiState.update {
            it.copy(showFilterTimeOptionDialog = !_uiState.value.showFilterTimeOptionDialog)
        }
    }

    fun onQueryChanged(value: String) {
        _uiState.update {
            it.copy(query = value)
        }

    }


    fun getFilterTimeOption(context: Context): DateUtils.FilterOption {
        return if (_uiState.value.selectedFilterTimeOption != null) {
            DateUtils.FilterOption.getFilterOption(_uiState.value.selectedFilterTimeOption?.id)
        } else {
            _uiState.value.startDate = DateUtils.getSalaryDate()
            _uiState.value.endDate = DateUtils.getCurrentDate()
            val timeFilterOptionId = SharedPreferenceManager.getFilterTimePeriod(context)
            _uiState.value.selectedFilterTimeOption =
                SelectedItemModel(id = timeFilterOptionId, value = "", isSelected = true)
            DateUtils.FilterOption.getFilterOption(timeFilterOptionId)
        }

    }

    fun wrapSendersToSenderComponentModel(
        sms: SmsEntity,
        context: Context
    ): SmsComponentModel {

        val sender = getSenderById(sms.senderId)

        return SmsComponentModel(
            id = sms.id,
            senderId = sms.senderId,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            isDeleted = sms.isDeleted,
            body = sms.body,
            senderIcon = sender?.senderIconUri ?: "",
            senderDisplayName = SenderModel.getDisplayName(context, sender),
            senderCategory = ContentModel.getDisplayName(context, sender?.content)

        )

    }

    private fun getSenderById(senderId: Int): SenderModel? {
        return _uiState.value.senders.find { it.id == senderId }

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


}