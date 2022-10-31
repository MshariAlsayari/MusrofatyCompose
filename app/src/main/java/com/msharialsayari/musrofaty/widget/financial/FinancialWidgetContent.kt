package com.msharialsayari.musrofaty.widget.financial


import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.MathUtils
import com.msharialsayari.musrofaty.utils.StringsUtils
import com.msharialsayari.musrofaty.widget.financial.FinancialWidget.Companion.FINANCIAL_WIDGET_INCOME_PREFS_KEY


@Composable
fun FinancialContent(
    modifier: GlanceModifier,
){

    val context = LocalContext.current
    val prefs = currentState<Preferences>()
    val income= prefs[intPreferencesKey(FINANCIAL_WIDGET_INCOME_PREFS_KEY)] ?: 457
    val expenses = prefs[intPreferencesKey(FINANCIAL_WIDGET_INCOME_PREFS_KEY)] ?: 2000
    val total = income + expenses
    val percentIncome = MathUtils.calculatePercentage(income.toDouble(),total.toDouble()).toInt()
    val percentExpenses = MathUtils.calculatePercentage(expenses.toDouble(),total.toDouble()).toInt()

    Box(modifier = modifier) {
        Box(
            modifier = GlanceModifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            ActionCompose()
        }

        Box(
            contentAlignment = Alignment.TopStart
        ) {
            AppLogo()
        }



        Box(
            modifier = GlanceModifier.padding(vertical = 25.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                PercentTextCompose(value = percentIncome.toString(), modifier = GlanceModifier.padding(8.dp).background(R.color.income_color))
                Text(
                    text = "%",
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                )
                PercentTextCompose(value = percentExpenses.toString(), modifier = GlanceModifier.padding(8.dp).background(R.color.expenses_color))
                Column {
                    InfoRowCompose(total = income.toLong(), isIncome = true)
                    InfoRowCompose(total = expenses.toLong(), isIncome = false)

                }

                Text(
                    text = Constants.CURRENCY_1
                )

            }
        }

    }



}

@Composable
fun AppLogo(
    modifier: GlanceModifier =GlanceModifier
){
    val context = LocalContext.current
    Text(
        text = context.getString(R.string.app_name),
        modifier = modifier
    )
}

@Composable
fun PercentTextCompose( modifier: GlanceModifier = GlanceModifier, value:String, textColor:ColorProvider?=ColorProvider(R.color.white)){
    Text(
        modifier = modifier.cornerRadius(16.dp).padding(horizontal = 8.dp),
        text = value,
        style = TextStyle(
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color =textColor ,
            textAlign = TextAlign.Center
        )

    )
}



@Composable
fun ActionCompose(modifier: GlanceModifier = GlanceModifier){
    Row(
        modifier = modifier
    ) {
        Image(
            provider = ImageProvider(
                resId = R.drawable.ic_refresh
            ),
            contentDescription = null,
            modifier = GlanceModifier
                .clickable(
                    onClick = actionRunCallback<RefreshFinancialClickAction>()
                )
        )

    }
}



@Composable
fun InfoRowCompose(
    total:Long,
    currency: String = Constants.CURRENCY_1,
    isIncome:Boolean = true
){

    Row(
        modifier = GlanceModifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = GlanceModifier
                .size(15.dp)
                .cornerRadius(16.dp)
                .background(
                    if (isIncome) R.color.income_color else R.color.expenses_color
                ),
            content = {}
        )


        Text(
            text = StringsUtils.formatLongNumbers(value = total),
            modifier = GlanceModifier.padding(horizontal = 8.dp)
        )


    }

}