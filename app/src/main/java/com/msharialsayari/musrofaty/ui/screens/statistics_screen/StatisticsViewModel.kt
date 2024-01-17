package com.msharialsayari.musrofaty.ui.screens.statistics_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoriesUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllCategory: GetCategoriesUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUIState())
    val uiState: StateFlow<StatisticsUIState> = _uiState


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
        val filterOption = DateUtils.FilterOption.getFilterOption(_uiState.value.selectedTimePeriod?.id)
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
                selectedCategory = selectedItem,
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

    fun navigateUp(){
        navigator.navigateUp()
    }
}