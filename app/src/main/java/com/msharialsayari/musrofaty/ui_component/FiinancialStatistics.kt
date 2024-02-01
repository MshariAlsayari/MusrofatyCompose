package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.data.PieEntry
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.DialogComponent.MessageDialog
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.MathUtils
import com.msharialsayari.musrofaty.utils.StringsUtils
import com.msharialsayari.musrofaty.utils.mirror

@Composable
fun FinancialStatistics(
    modifier: Modifier = Modifier,
    model: FinancialStatisticsModel,
    onClicked: (List<SmsModel>, Boolean) -> Unit

) {
    val percentIncome = MathUtils.calculatePercentage(model.incomeTotal, model.total)
    val percentExpenses = MathUtils.calculatePercentage(model.expensesTotal, model.total)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin10))
    ) {

        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ChartComponent.FinancialPieChartCompose(
                entries = arrayListOf(
                    PieEntry(percentIncome.toFloat(), ""),
                    PieEntry(percentExpenses.toFloat(), "")
                ),
                colors = arrayListOf(
                    MusrofatyTheme.colors.incomeColor,
                    MusrofatyTheme.colors.expensesColor
                )
            )

        }

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            FinancialStatisticsInfo(
                modifier = Modifier.clickable {
                    onClicked(model.incomesSmsList, false)
                },
                total = StringsUtils.formatNumberWithComma(model.incomeTotal.toString()),
                currency = model.currency,
                isIncome = true
            )

            DividerComponent.HorizontalDividerComponent(modifier = modifier)


            FinancialStatisticsInfo(
                modifier = Modifier.clickable {
                    onClicked(model.expensesSmsList,true)
                },
                total = StringsUtils.formatNumberWithComma(model.expensesTotal.toString()),
                currency = model.currency,
                isIncome = false
            )
        }
    }


}

@Composable
fun FinancialStatisticsInfo(
    modifier: Modifier = Modifier,
    total: String,
    currency: String,
    isIncome: Boolean,
) {

    val openDialog = rememberSaveable { mutableStateOf(false) }

    if (openDialog.value) {
        MessageDialog(
            message = R.string.no_currency_message,
            onDismiss = {
                openDialog.value = false
            })
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.default_margin10), horizontal = dimensionResource(id = R.dimen.default_margin16)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RectangleShape)
                .background(
                    if (isIncome) MusrofatyTheme.colors.incomeColor else MusrofatyTheme.colors.expensesColor
                )
        )

        TextComponent.BodyText(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            text = if (isIncome) stringResource(id = R.string.income) else stringResource(id = R.string.expenses),
            alignment = TextAlign.Start
        )

        TextComponent.BodyText(
            modifier = Modifier.weight(1f),
            text = total,
            alignment = TextAlign.Center
        )

        if (currency.isEmpty()) {
            TextComponent.ClickableText(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        openDialog.value = true
                    },
                text = stringResource(id = R.string.common_click),
                alignment = TextAlign.End
            )
        } else {
            TextComponent.BodyText(
                modifier = Modifier.weight(1f),
                text = currency,
                alignment = TextAlign.End
            )
        }

        Icon(
            Icons.Default.KeyboardArrowRight,
            modifier = Modifier
                .mirror(),
            contentDescription = null
        )


    }


}

data class FinancialStatisticsModel(
    val filterOption: DateUtils.FilterOption? = DateUtils.FilterOption.ALL,
    val currency: String = "",
    val expensesTotal: Double = 0.0,
    val incomeTotal: Double = 0.0,
    val total: Double = 0.0,
    val expensesSmsList: List<SmsModel> = listOf(),
    val incomesSmsList: List<SmsModel> = listOf()
)