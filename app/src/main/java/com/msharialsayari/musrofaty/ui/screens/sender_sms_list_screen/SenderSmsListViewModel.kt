package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.excei.ExcelModel
import com.msharialsayari.musrofaty.excei.ExcelUtils
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.pdf.PdfCreatorActivity
import com.msharialsayari.musrofaty.pdf.PdfCreatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharingFileUtils
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
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SenderSmsListUiState(sender = SenderModel(senderName = "")))
    val uiState: StateFlow<SenderSmsListUiState> = _uiState

    fun initSender(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val senderResult = getSenderUseCase.invoke(senderId)
            if (senderResult != null){
                 getFilters(senderResult.id)
                _uiState.update {
                    it.copy(
                        sender = senderResult,
                    )
                }
            }

            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }


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

    fun generateExcelFile(activity: Activity) {

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
                activity,
                Constants.EXCEL_FILE_NAME
            ).exportDataIntoWorkbook(excelModel)

            _uiState.update {
                it.copy(showGeneratingExcelFileDialog = false)
            }

            if (isGenerated) {
                val fileURI = SharingFileUtils.accessFile(activity, Constants.EXCEL_FILE_NAME)
                val intent = SharingFileUtils.createSharingIntent(activity, fileURI)
                activity.startActivity(intent)
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

    fun navigateToSenderDetails(senderId: Int){
        navigator.navigate(Screen.SenderDetailsScreen.route + "/${senderId}")
    }

    fun navigateToFilterScreen(senderId: Int, filterId: Int?) {
        if (filterId == null)
            navigator.navigate(Screen.FilterScreen.route + "/${senderId}")
        else
            navigator.navigate(Screen.FilterScreen.route + "/${senderId}" + "/${filterId}")
    }

    fun navigateToSmsDetails(smsID: String) {
        navigator.navigate(Screen.SmsScreen.route + "/${smsID}")
    }

    fun navigateBack(){
        navigator.navigateUp()
    }

    fun navigateToPDFActivity(activity: Activity,pdfBundle: PdfCreatorViewModel.PdfBundle) {
        PdfCreatorActivity.startPdfCreatorActivity(activity,pdfBundle)
    }


}