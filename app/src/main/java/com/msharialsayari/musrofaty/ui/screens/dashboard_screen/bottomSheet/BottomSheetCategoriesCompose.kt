package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.bottomSheet


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.github.mikephil.charting.utils.ColorTemplate
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryStatisticsModel
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.categoriesStatistics.CategoriesDashboardStatistics


@Composable
fun BottomSheetCategoriesCompose(viewModel: DashboardViewModel){

    val uiState by viewModel.uiState.collectAsState()
    val categoriesList = uiState.categoriesStatistics.values.toList()

    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16)),
    ) {
        BottomSheetSectionHeader(viewModel, R.string.tab_categories_statistics)
        when {
            uiState.isFinancialStatisticsSmsPageLoading ->  ProgressBar.CircleProgressBar()
            categoriesList.isEmpty()                    ->  EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
            else                                        ->  CategoryCompose(viewModel)
        }

    }

}

@Composable
private fun CategoryCompose(viewModel: DashboardViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val colors: ArrayList<Int> = ArrayList()
    colors.addAll(ColorTemplate.VORDIPLOM_COLORS.toList())
    colors.addAll(ColorTemplate.JOYFUL_COLORS.toList())
    colors.addAll(ColorTemplate.COLORFUL_COLORS.toList())
    colors.addAll(ColorTemplate.LIBERTY_COLORS.toList())
    colors.addAll(ColorTemplate.PASTEL_COLORS.toList())
    colors.add(ColorTemplate.getHoloBlue())
    val categories = uiState.categoriesStatistics.values.mapIndexed { index, it ->
        it.toCategoryStatisticsModel(
            context,
            colors[index]
        )
    }
   CategoriesDashboardStatistics(categories = categories)

}