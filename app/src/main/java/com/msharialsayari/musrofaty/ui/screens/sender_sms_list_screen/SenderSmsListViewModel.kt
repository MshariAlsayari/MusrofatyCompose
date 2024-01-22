package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.app.Activity
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.google.gson.Gson
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFiltersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.LoadSenderSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObservingPaginationAllSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirebaseUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateSenderIconUseCase
import com.msharialsayari.musrofaty.excei.ExcelModel
import com.msharialsayari.musrofaty.excei.ExcelUtils
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.pdf.PdfCreatorActivity
import com.msharialsayari.musrofaty.pdf.PdfCreatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.SenderSmsListBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs.SenderSmsListScreenTabs
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets.BottomSheetType
import com.msharialsayari.musrofaty.ui_component.CategoryStatisticsModel
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
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
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
    private val getCategoriesStatisticsUseCase: GetCategoriesStatisticsUseCase,
    private val getFiltersUseCase: GetFiltersUseCase,
    private val getAllSmsModelUseCase: GetSmsModelListUseCase,
    private val loadSenderSmsUseCase: LoadSenderSmsUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val updateSenderIconUseCase: UpdateSenderIconUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val postStoreToFirebaseUseCase: PostStoreToFirebaseUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
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
        getCategories()
    }

    private fun initSender() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val senderResult = getSenderUseCase.invoke(senderId)
            if (senderResult != null) {
                _uiState.update {
                    it.copy(
                        sender = senderResult,
                    )
                }
                getFilters(senderResult.id)
                getData()
            }else{
                navigator.navigateUp()
            }

            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

     private fun getCategories() {
        viewModelScope.launch {
            val categoriesResult = getCategoriesUseCase.invoke()
            _uiState.update {
                it.copy(
                    categories = categoriesResult
                )
            }
        }
    }


    fun getData() {
        getSmsList()
        getFilters(senderId)
        getStatisticsData()
        getSmsListTabs()
    }

    private fun getStatisticsData(){
        val selectedTabIndex = _uiState.value.selectedTabIndex
        when(SenderSmsListScreenTabs.getTabByIndex(selectedTabIndex)){
            SenderSmsListScreenTabs.FINANCIAL -> getFinancialStatistics()
            SenderSmsListScreenTabs.CATEGORIES -> getCategoriesStatistics()
            else -> {}
        }
    }

    fun refreshSms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val senderName = _uiState.value.sender.senderName
            loadSenderSmsUseCase.invoke(senderName)
            getData()
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

    private fun getSmsListTabs() {
        viewModelScope.launch {

            val allSmsTab :Flow<PagingData<SmsModel>> = observingPaginationAllSmsUseCase(
                senderId =  senderId,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                isDeleted = null,
                isFavorite = null,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )

            val favoriteSmsTab :Flow<PagingData<SmsModel>> = observingPaginationAllSmsUseCase(
                senderId =  senderId,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                isDeleted = null,
                isFavorite = true,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )


            val softDeletedSmsTab :Flow<PagingData<SmsModel>> = observingPaginationAllSmsUseCase(
                senderId =  senderId,
                filterOption = getFilterTimeOption(),
                query = getFilterWord(),
                isDeleted = true,
                isFavorite = null,
                startDate = _uiState.value.startDate,
                endDate = _uiState.value.endDate,
            )

            _uiState.update { state ->
                state.copy(
                    allSmsList = allSmsTab,
                    favoriteSmsList = favoriteSmsTab,
                    softDeletedSmsList = softDeletedSmsTab
                )
            }
        }

    }


     private fun getSmsList() {
         viewModelScope.launch {
             val result = getAllSmsModel()

             _uiState.update { state ->
                 state.copy(
                     smsList = result,
                 )
             }
         }

    }

    private suspend fun getAllSmsModel(isDeleted: Boolean?=null): List<SmsModel> {
        return getAllSmsModelUseCase.invoke(
            senderId = _uiState.value.sender.id,
            filterOption = getFilterTimeOption(),
            query = getFilterWord(),
            isDeleted = isDeleted,
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
                    financialTabLoading = true,
                )
            }

            val smsList = getAllSmsModel(isDeleted = false)
            val result = getFinancialStatisticsUseCase.invoke(smsList)
            _uiState.update { state ->
                state.copy(
                    financialTabLoading = false,
                    financialStatistics = result,
                )
            }


        }
    }

    fun getCategoriesStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(categoriesTabLoading = true) }
            val smsList = getAllSmsModel(isDeleted = false)
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
                    categoriesTabLoading = false
                )
            }


        }
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
        return DateUtils.FilterOption.getFilterOptionOrDefault(_uiState.value.selectedFilterTimeOption?.id)
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


    fun onDatePeriodsSelected(start: Long, end: Long){
        _uiState.update {
            it.copy(startDate = start,endDate = end)
        }

    }

    fun updateBottomSheetType(type: SenderSmsListBottomSheetType?){
        _uiState.update {
            it.copy(
                bottomSheetType = type,
            )
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
            val senderId = _uiState.value.sender.id
            updateSenderIconUseCase.invoke(senderId, iconPath)
        }
    }

    fun addCategory(model: CategoryModel) {
        viewModelScope.launch {
            addCategoryUseCase.invoke(model)
        }
    }

    fun onCategorySelected(item: SelectedItemModel) {
        viewModelScope.launch {
            val categoryId = item.id
            val storeName = _uiState.value.selectedSms?.storeAndCategoryModel?.store?.name
            val storeModel = storeName?.let { name -> StoreModel(name = name, categoryId = categoryId) }

            storeModel?.let {
                addOrUpdateStoreUseCase.invoke(it)
                postStoreToFirebaseUseCase.invoke(storeModel.toStoreEntity())
                getData()
            }
        }
    }

    fun getCategoryItems(
        context: Context,
        categories: List<CategoryEntity>
    ): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        categories.map { value ->
            list.add(
                SelectedItemModel(
                    id = value.id,
                    value = CategoryModel.getDisplayName(context, value),
                    isSelected = _uiState.value.selectedSms?.storeAndCategoryModel?.category?.id == value.id
                )
            )
        }

        return list

    }

    fun onSmsCategoryClicked(item: SmsModel) {
        _uiState.update {
            it.copy(selectedSms = item)
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

    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")
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