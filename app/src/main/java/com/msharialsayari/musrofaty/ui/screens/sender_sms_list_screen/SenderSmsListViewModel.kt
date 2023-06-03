package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.excei.ExcelModel
import com.msharialsayari.musrofaty.excei.ExcelUtils
import com.msharialsayari.musrofaty.pdf.PdfCreatorViewModel
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenderSmsListViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getAllSms: GetAllSmsUseCase,
    private val getFavoriteSmsUseCase: GetFavoriteSmsUseCase,
    private val getSoftDeletedSmsUseCase: GetSoftDeletedSmsUseCase,
    private val getSmsBySenderIdWithDeleteCheckUseCase: GetSmsBySenderIdWithDeleteCheckUseCase,
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
    private val getCategoriesStatisticsUseCase: GetCategoriesStatisticsUseCase,
    private val getFiltersUseCase: GetFiltersUseCase,
    private val getAllSmsUseCase: GetSmsListUseCase,
    private val loadSenderSmsUseCase: LoadSenderSmsUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val updateSenderIconUseCase: UpdateSenderIconUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SenderSmsListUiState())
    val uiState: StateFlow<SenderSmsListUiState> = _uiState


    fun getDate() {
        val senderId = _uiState.value.sender?.id!!
        getAllSms(senderId)
        getFavoriteSms(senderId)
        getFinancialStatistics(senderId)
        getCategoriesStatistics(senderId)
        getAllSmsBySenderId(senderId)
    }

    fun refreshSms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val senderName = _uiState.value.sender?.senderName
            senderName?.let { loadSenderSmsUseCase.invoke(it) }
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }


    fun onTabSelected(tabIndex: Int) {
        _uiState.update {
            it.copy(selectedTabIndex = tabIndex)
        }
    }


    fun getSender(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val senderResult = getSenderUseCase.invoke(senderId)
            if (senderResult != null)
                getFilters(senderResult.id)
            _uiState.update {
                it.copy(
                    sender = senderResult,
                    navigateBack = senderResult == null,
                    isLoading = false
                )
            }
        }
    }

    private fun getFilters(senderId: Int) {
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


    fun getAllSms(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAllSmsPageLoading = false) }
            val smsResult = getAllSms.invoke(
                senderId,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )
            _uiState.update {
                it.copy(
                    smsFlow = smsResult,
                    isAllSmsPageLoading = false
                )
            }
        }

    }

    fun getAllSmsBySenderId(senderId: Int) {
        viewModelScope.launch {
            val smsResult = getSmsBySenderIdWithDeleteCheckUseCase.invoke(
                senderId,
                isDeleted =false,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
            )
            _uiState.update {
                it.copy(
                    allSmsFlow = smsResult
                )
            }
        }

    }

    fun getFavoriteSms(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFavoriteSmsPageLoading = false) }
            val smsResult = getFavoriteSmsUseCase.invoke(
                senderId,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
            )
            _uiState.update {
                it.copy(
                    favoriteSmsFlow = smsResult,
                    isFavoriteSmsPageLoading = false
                )
            }
        }

    }


    fun getSoftDeletedSms(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSoftDeletedSmsPageLoading = false) }
            val smsResult = getSoftDeletedSmsUseCase.invoke(
                senderId,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
            )
            _uiState.update {
                it.copy(
                    softDeletedSmsFlow = smsResult,
                    isSoftDeletedSmsPageLoading = false
                )
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

    fun getFinancialStatistics(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFinancialStatisticsSmsPageLoading = true) }
            val smsResult = getSmsBySenderIdWithDeleteCheckUseCase.invoke(
                senderId,
                isDeleted = false,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
            )
            smsResult.collectLatest { list ->
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

    fun getCategoriesStatistics(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCategoriesStatisticsSmsPageLoading = true) }
            val smsResult = getSmsBySenderIdWithDeleteCheckUseCase.invoke(
                senderId,
                isDeleted = false,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
            )
            smsResult.collectLatest { list ->
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
            senderId = sms.senderId,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            isDeleted = sms.isDeleted,
            body = sms.body,
            senderIcon = _uiState.value.sender?.senderIconUri ?:"",
            senderDisplayName = SenderModel.getDisplayName(context, _uiState.value.sender),
            senderCategory = ContentModel.getDisplayName(context, _uiState.value.sender?.content)

        )

    }


    fun getFilterOptions(selectedItem: SelectedItemModel? = null): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        _uiState.value.filters.mapIndexed { index, value ->
            list.add(
                SelectedItemModel(
                    id = value.id,
                    value = value.title,
                    isSelected = selectedItem?.id == value.id
                )
            )
        }

        return list

    }

    fun getFilterTimeOption(): DateUtils.FilterOption {
        return DateUtils.FilterOption.getFilterOption(_uiState.value.selectedFilterTimeOption?.id)
    }

    private fun getFilterWord(): String {
        return _uiState.value.filters.find { it.id == _uiState.value.selectedFilter?.id }?.words
            ?: ""
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
            it.copy(showStartDatePicker = false, showEndDatePicker = false)
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

    fun getPdfBundle(): PdfCreatorViewModel.PdfBundle {
        return PdfCreatorViewModel.PdfBundle(
            senderId = _uiState.value.sender?.id ?: 0,
            filterTimeId = uiState.value.selectedFilterTimeOption?.id ?: 0,
            filterWord = getFilterWord(),
            startDate = uiState.value.startDate,
            endDate = uiState.value.endDate
        )
    }

    fun generateExcelFile(context: Context, onFileGenerated: () -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(showGeneratingExcelFileDialog = true)
            }
            val result = getAllSmsUseCase.invoke(
                senderId = _uiState.value.sender?.id ?: 0,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate
            )
            val excelModel = ExcelModel(smsList = result)
            val isGenerated = ExcelUtils(
                context,
                Constants.EXCEL_FILE_NAME
            ).exportDataIntoWorkbook(excelModel)
            if (isGenerated) {
                onFileGenerated()
            }

            _uiState.update {
                it.copy(showGeneratingExcelFileDialog = false)
            }
        }
    }

    fun onFilterTimeOptionSelected(item: SelectedItemModel) {
        _uiState.update {
            it.copy(selectedFilterTimeOption = item)
        }
    }


    fun updateSenderIcon(iconPath: String) {
        viewModelScope.launch {
            val senderId = _uiState.value.sender?.id
            senderId?.let {
                updateSenderIconUseCase.invoke(it, iconPath)
            }


        }
    }


    data class SenderSmsListUiState(
        var selectedTabIndex: Int = 0,
        var isLoading: Boolean = false,
        var navigateBack: Boolean = false,
        var isAllSmsPageLoading: Boolean = false,
        var isFavoriteSmsPageLoading: Boolean = false,
        var isSoftDeletedSmsPageLoading: Boolean = false,
        var isFinancialStatisticsSmsPageLoading: Boolean = false,
        var isCategoriesStatisticsSmsPageLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        val sender: SenderModel? = null,
        var smsFlow: Flow<PagingData<SmsEntity>>? = null,
        var favoriteSmsFlow: Flow<PagingData<SmsEntity>>? = null,
        var softDeletedSmsFlow: Flow<PagingData<SmsEntity>>? = null,
        var allSmsFlow: Flow<List<SmsEntity>>? = null,
        var selectedFilterTimeOption: SelectedItemModel? = null,
        var selectedFilter: SelectedItemModel? = null,
        var filters: List<FilterAdvancedModel> = emptyList(),
        var financialStatistics: Map<String, FinancialStatistics> = emptyMap(),
        var categoriesStatistics: Map<Int, CategoryStatistics> = emptyMap(),
        var startDate: Long = 0,
        var endDate: Long = 0,
        var showStartDatePicker: Boolean = false,
        var showEndDatePicker: Boolean = false,
        var showGeneratingExcelFileDialog: Boolean = false
    )
}