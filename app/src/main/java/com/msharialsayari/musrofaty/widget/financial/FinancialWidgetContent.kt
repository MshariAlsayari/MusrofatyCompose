package com.msharialsayari.musrofaty.widget.financial


import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.Constants


@Composable
fun FinancialContent(
    modifier: GlanceModifier,
){



    Row(
        modifier = modifier,
    ) {

        Text(  text = "40" )
        Text(  text = "60" )
        Column {
            Row {
                TitleCompose()
                ActionCompose()
            }

            InfoRowCompose(total = "50", isIncome = true)
            InfoRowCompose(total = "50", isIncome = false)
        }



    }

}



@Composable
fun ActionCompose(
    modifier: GlanceModifier = GlanceModifier,
){
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
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
                .defaultWeight()
        )

    }
}

@Composable
fun TitleCompose(
){
    val context = LocalContext.current
    Text(
        text = context.getString(R.string.tab_financial_statistics),
        style = TextStyle(textAlign = androidx.glance.text.TextAlign.Center)
    )

}

@Composable
fun InfoRowCompose(
    total:String,
    currency: String = Constants.CURRENCY_1,
    isIncome:Boolean = true
){

    val context = LocalContext.current

    Row(
        modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        Box(
            modifier = GlanceModifier
                .size(20.dp)
                .background(
                    if (isIncome) R.color.income_color else R.color.expenses_color
                ),
            content = {}
        )


        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = if (isIncome) context.getString( R.string.income) else context.getString( R.string.expenses)
            )

            Text(
                text = total
            )

            Text(
                text = currency
            )
        }
    }

}