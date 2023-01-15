package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.github.mikephil.charting.data.PieEntry
import com.msharialsayari.musrofaty.R

@Composable
fun  CategoriesStatistics(modifier: Modifier = Modifier,
                          categories   : List<CategoryStatisticsModel>,
                          onSmsClicked : (String)->Unit
){

    val percentList =ArrayList(categories.map { PieEntry(it.percent.toFloat(), "") })
    val colorList = ArrayList(categories.map { it.color})


    Column(modifier = modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin10))
    ) {

        Box(
            modifier = Modifier
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
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))
        ) {
            categories.forEach {
                RowComponent.ExpandableCategoryStatisticsRow(model = it, onClick = { smsId ->
                    onSmsClicked(smsId)
                })
                DividerComponent.HorizontalDividerComponent(modifier = modifier)
            }


        }




    }

}

