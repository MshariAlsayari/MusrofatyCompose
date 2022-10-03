package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.data.PieEntry
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.MathUtils
import com.msharialsayari.musrofaty.utils.StringsUtils

@Composable
fun FinancialStatistics(modifier:Modifier=Modifier,
                        model: FinancialStatisticsModel,
){
    val context         = LocalContext.current
    val percentIncome   = MathUtils.calculatePercentage(model.incomeTotal,model.total)
    val percentExpenses = MathUtils.calculatePercentage(model.expensesTotal,model.total)
    Column(modifier = modifier
        .fillMaxWidth()
        .height(250.dp),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            ChartComponent.PieChartCompose(
                entries = arrayListOf(
                    PieEntry(percentIncome.toFloat(), ""),
                    PieEntry(percentExpenses.toFloat(), "")
                ),
                colors = arrayListOf(
                    context.getColor(R.color.incomeColor),
                    context.getColor(R.color.expensesColor)
                )
            )



        }

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))
        ) {
            FinancialStatisticsInfo(
                total = StringsUtils.formatNumberWithComma(model.incomeTotal.toString()),
                currency = model.currency,
                isIncome = true
            )
            FinancialStatisticsInfo(
                total = StringsUtils.formatNumberWithComma(model.expensesTotal.toString()),
                currency = model.currency,
                isIncome = false
            )
        }

        DividerComponent.HorizontalDividerComponent()
    }


}

@Composable
fun FinancialStatisticsInfo(modifier:Modifier=Modifier,
                            total: String,
                            currency: String,
                            isIncome:Boolean
){

    Row( modifier = modifier.fillMaxWidth().padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RectangleShape)
                .background(
                    if (isIncome) colorResource(id = R.color.incomeColor) else colorResource(id = R.color.expensesColor)
                )
        )


        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            TextComponent.BodyText(
                text = if (isIncome) stringResource(id = R.string.income) else stringResource(id = R.string.expenses)
            )

            TextComponent.BodyText(
                text = total
            )

            TextComponent.BodyText(
                text = currency
            )
        }
    }



}

data class FinancialStatisticsModel(
    val filterOption: DateUtils.FilterOption? = DateUtils.FilterOption.ALL,
    val currency: String = "",
    val expensesTotal: Double = 0.0,
    val incomeTotal: Double = 0.0,
    val total: Double = 0.0,

    )