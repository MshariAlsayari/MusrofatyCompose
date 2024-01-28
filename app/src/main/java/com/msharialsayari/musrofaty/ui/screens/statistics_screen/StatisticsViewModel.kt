package com.msharialsayari.musrofaty.ui.screens.statistics_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChartModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesStatisticsChartUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets.BottomSheetType
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllCategory: GetCategoriesUseCase,
    private val getAllSmsModelUseCase: GetSmsModelListUseCase,
    private val getCategoriesStatisticsChartUseCase: GetCategoriesStatisticsChartUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUIState())
    val uiState: StateFlow<StatisticsUIState> = _uiState

    companion object {
        private val TAG = StatisticsViewModel::class.java.simpleName
    }

    fun observingCategories(): Flow<List<CategoryEntity>> {
        return getAllCategory.invoke()

    }


    fun getSelectedCategory(selectedItem: SelectedItemModel?, list :List<CategoryEntity>): String {
        val selectedCategory = list.find { it.id ==  selectedItem?.id}
        return if(selectedCategory == null){
            context.getString(R.string.common_no_category)
        }else{
            CategoryModel.getDisplayName(context,selectedCategory)
        }

    }


    fun getFilterTimeOption(): String {
        val filterOption = DateUtils.FilterOption.getFilterOptionOrDefault(id = _uiState.value.selectedTimePeriod?.id, default = DateUtils.FilterOption.MONTH)
        return if(filterOption== DateUtils.FilterOption.RANGE){
             DateUtils.formattedRangeDate(_uiState.value.startDate,_uiState.value.endDate)
        }else{
            context.getString(filterOption.title)
        }
    }

    fun updateSelectedFilterTimePeriods(selectedItem: SelectedItemModel?){
        _uiState.update {
            it.copy(
                selectedTimePeriod = selectedItem,
            )
        }
    }

    fun updateSelectedCategory(selectedItem: SelectedItemModel?){
        _uiState.update {
            it.copy(
                selectedCategory = if(selectedItem?.isSelected == true) selectedItem else null,
            )
        }
    }

    fun updateBottomSheetType(type: BottomSheetType?){
        _uiState.update {
            it.copy(
                bottomSheetType = type,
            )
        }
    }

    fun getSmsList(){
        viewModelScope.launch {
            Log.d(TAG , "getSmsList()")
            val startDate  = _uiState.value.startDate
            val endDate    = _uiState.value.endDate
            val timeOption = DateUtils.FilterOption.getFilterOptionOrDefault(id = _uiState.value.selectedTimePeriod?.id , default = DateUtils.FilterOption.MONTH)
            val category   = _uiState.value.selectedCategory?.id

            _uiState.update {
                it.copy(
                    loading = true ,
                )
            }


            val smsList = getAllSmsModelUseCase.invoke(
                filterOption = timeOption,
                startDate = startDate,
                endDate = endDate,
                categoryId = category
            )


            val groupSmsByCurrent = smsList.groupBy { it.currency }
            val charts = mutableListOf<CategoriesChartModel>()
            groupSmsByCurrent.forEach { (key, value) ->
                val result = getCategoriesStatisticsChartUseCase.invoke(key,timeOption,value)
                if (result.total > 0)
                    charts.add(result)
                Log.d(TAG , "chart() title:$key result: ${value.size}")

            }


            _uiState.update {
                it.copy(
                    list = smsList,
                    charts = charts,
                    loading = false ,
                )
            }

        }
    }

    fun addCategory(model: CategoryModel) {
        viewModelScope.launch {
            addCategoryUseCase.invoke(model)
        }
    }


    fun onDatePeriodsSelected(start: Long, end: Long){
        _uiState.update {
            it.copy(startDate = start,endDate = end)
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
                    isSelected = _uiState.value.selectedCategory?.id == value.id
                )
            )
        }

        return list

    }

    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")
    }

    fun navigateUp(){
        navigator.navigateUp()
    }
}