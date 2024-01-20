package com.msharialsayari.musrofaty.ui_component.categoriesStatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource

import com.github.mikephil.charting.data.PieEntry
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryStatisticsModel
import com.msharialsayari.musrofaty.ui_component.CategoryStatisticsModel
import com.msharialsayari.musrofaty.ui_component.ChartComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.RowComponent

@Composable
fun CategoriesStatistics(
    modifier: Modifier = Modifier,
    item: CategoryContainerStatistics,
    onRowClicked:(CategoryStatisticsModel)->Unit
) {

    val context = LocalContext.current
    val percentList = ArrayList(item.data.map { PieEntry(it.value.payPercent.toFloat(), "") })
    val colorList = ArrayList(item.data.map { it.value.color })

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin10))
    ) {
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ChartComponent.PieChartCompose(
                title = item.getTitle(),
                entries = percentList,
                colors = colorList
            )

        }

        Column(
            modifier = Modifier,
        ) {
            item.data.forEach {
                RowComponent.CategoryStatisticsRow(model = it.value.toCategoryStatisticsModel(context = context), onClicked = {
                    onRowClicked(it)
                })

            }


        }


    }

}

