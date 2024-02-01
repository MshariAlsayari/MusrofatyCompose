package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.statistics


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.categoriesStatistics.CategoriesStatistics


@Composable
fun CategoriesContent(viewModel: DashboardViewModel){

    val uiState by viewModel.uiState.collectAsState()
    val categoriesList = uiState.categoriesStatistics

    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16)),
    ) {
        BottomSheetSectionHeader(viewModel, R.string.tab_categories_statistics)
        when {
            uiState.isCategoriesStatisticsSmsPageLoading ->  ProgressBar.CircleProgressBar()
            categoriesList.isEmpty()                    ->  EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
            else                                        ->  CategoryCompose(viewModel)
        }

    }

}

@Composable
private fun CategoryCompose(viewModel: DashboardViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val categoriesList = uiState.categoriesStatistics
    Column {
        categoriesList.forEach {
            CategoriesStatistics(item = it, onRowClicked = {categoryModel->
                val ids = categoryModel.smsList.map { it.id }
                val smsContainer = SmsContainer(ids)
                viewModel.navigateToSmsListScreen(
                    smsContainer,
                    categoryModel.storeAndCategoryModel?.category,
                    isCategoryRowClicked = true,
                    isExpensesSmsRowClicked = false,
                    context
                )
            })
        }
    }






}