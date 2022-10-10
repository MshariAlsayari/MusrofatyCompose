package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.github.mikephil.charting.data.PieEntry

@Composable

fun  CategoriesStatistics(modifier: Modifier = Modifier,
                          categories   : List<CategoryStatisticsModel>,
                          onSmsClicked : (String)->Unit
){

    val percentList =ArrayList(categories.map { PieEntry(it.percent.toFloat(), "") })
    val colorList = ArrayList(categories.map { it.color})


    Column(modifier = modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            ChartComponent.PieChartCompose(
                entries = percentList,
                colors = colorList
            )

        }

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))
        ) {
            categories.forEach {
                RowComponent.CategoryStatisticsRow(model = it, onClick = { smsId->
                    onSmsClicked(smsId)
                })
                DividerComponent.HorizontalDividerComponent(modifier = modifier)
            }


        }

        DividerComponent.HorizontalDividerComponent(modifier = modifier)
    }

}

