package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.app.Activity
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFiltersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.LoadSenderSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingPaginationAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateSenderIconUseCase
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenderSmsListViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase,
    private val observingPaginationAllSmsUseCase: ObservingPaginationAllSmsUseCase,
    private val observingAllSmsUseCase: ObservingAllSmsUseCase,
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
    private val getCategoriesStatisticsUseCase: GetCategoriesStatisticsUseCase,
    private val getFiltersUseCase: GetFiltersUseCase,
    private val getAllSmsModelUseCase: GetSmsModelListUseCase,
    private val getAllSmsUseCase: GetSmsListUseCase,
    private val loadSenderSmsUseCase: LoadSenderSmsUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val updateSenderIconUseCase: UpdateSenderIconUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SenderSmsListUiState(sender = SenderModel(senderName = "")))
    val uiState: StateFlow<SenderSmsListUiState> = _uiState

    val senderId: Int
        get() {
            return savedStateHandle.get<Int>("senderId")!!
        }
    
    init{
        initSender()
    }

    private fun initSender() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val senderResult = getSenderUseCase.invoke(senderId)
            if (senderResult != null) {
                getFilters(senderResult.id)
                getDate()
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
        getFinancialStatistics()
        getCategoriesStatistics()
    }

    fun refreshSms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val senderName = _uiState.value.sender.senderName
            loadSenderSmsUseCase.invoke(senderName)
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
            val filtersResult = getFiltersUseCase.invoke(senderId)
            _uiState.update {
                it.copy(filters = filtersResult,)
            }
        }
    }



    fun observingPaginationAllSms(isDeleted:Boolean?, isFavorite:Boolean?): Flow<PagingData<SmsEntity>> {
        return observingPaginationAllSmsUseCase(
            senderId =  senderId,
            filterOption = getFilterTimeOption(),
            query = getFilterWord(),
            isDeleted = isDeleted,
            isFavorite = isFavorite,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate,
        )
    }

    fun observingAllSms(): Flow<List<SmsEntity>> {
        return observingAllSmsUseCase(
            senderId =  senderId,
            filterOption = getFilterTimeOption(),
            query = getFilterWord(),
            isDeleted = false,
            isFavorite = null,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate,
        )
    }

    private suspend fun getAllSmsModel(): List<SmsModel> {
        return getAllSmsModelUseCase.invoke(
            senderId = _uiState.value.sender.id,
            filterOption = getFilterTimeOption(),
            query = getFilterWord(),
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate
        )
    }

    private suspend fun getAllSms(): List<SmsEntity> {
        return getAllSmsUseCase.invoke(
            senderId = _uiState.value.sender.id,
            filterOption = getFilterTimeOption(),
            isDeleted = false,
            query = getFilterWord(),
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate
        )
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

    fun getFinancialStatistics() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    financialLoading = true,
                )
            }

            val smsList = getAllSms()
            val result = getFinancialStatisticsUseCase.invoke(smsList)
            _uiState.update { state ->
                state.copy(
                    financialLoading = false,
                    financialStatistics = result,
                )
            }


        }
    }

    fun getCategoriesStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(categoriesTabLoading = true) }
            val smsList = getAllSms()
            val result = getCategoriesStatisticsUseCase.invoke(smsList)
            _uiState.update { state ->
                state.copy(
                    categoriesStatistics = result,
                    categoriesTabLoading = false
                )
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
        return _uiState.value.filters.find { it.id == _uiState.value.selectedFilter?.id }?.words ?: ""
    }

    fun updateSelectedFilterWord(selectedItem: SelectedItemModel?){
        _uiState.update {
            it.copy(
                selectedFilter = selectedItem,
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
            senderId = _uiState.value.sender.id,
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
            val result = getAllSmsModel()
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