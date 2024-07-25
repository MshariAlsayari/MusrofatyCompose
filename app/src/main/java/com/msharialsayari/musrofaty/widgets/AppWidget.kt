package com.msharialsayari.musrofaty.widgets

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.MainActivity
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.DateUtils

class AppWidget : GlanceAppWidget() {

    companion object {
        private val TAG = AppWidget::class.java.simpleName

        val TODAY_FINANCIAL_INFO = stringPreferencesKey("today")
        val WEEK_FINANCIAL_INFO  = stringPreferencesKey("week")
        val MONTH_FINANCIAL_INFO = stringPreferencesKey("month")
        val LOADING_WIDGET_PREF_KEY = booleanPreferencesKey("loading")
    }


    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                ContentView()
            }
        }
    }

    @Composable
    private fun ContentView() {
        Column(
            modifier = GlanceModifier.padding(16.dp).fillMaxSize()
                .background(
                    ColorProvider(
                        day = MyAppWidgetGlanceColorScheme.backgroundColor,
                        night = MyAppWidgetGlanceColorScheme.backgroundColorDark
                    )
                ).clickable(
                    onClick = actionRunCallback<NavigateWidgetActionCallback>()
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            HeaderView(GlanceModifier.fillMaxWidth())
            MainView(GlanceModifier.fillMaxWidth())

        }
    }


    @Composable
    private fun HeaderView(modifier: GlanceModifier = GlanceModifier){
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleView(modifier = GlanceModifier.defaultWeight())
            RefreshView(modifier = GlanceModifier)
        }

    }

    @Composable
    private fun MainView(modifier: GlanceModifier = GlanceModifier){
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateView(modifier = GlanceModifier.padding(horizontal = 16.dp))
            DividerView(modifier = GlanceModifier)
            StatisticsView(modifier = GlanceModifier.defaultWeight().padding(start = 16.dp))
        }

    }

    @Composable
    private fun RefreshView(modifier: GlanceModifier = GlanceModifier){

        val isLoading =  currentState(key = LOADING_WIDGET_PREF_KEY) ?: false

        val icon = if (isLoading) R.drawable.ic_spinner else R.drawable.ic_refresh


        Image(
            provider = ImageProvider(icon),
            modifier = modifier.clickable(
                onClick = actionRunCallback<RefreshAction>()
            ),
            contentDescription = "Refresh",
            colorFilter = ColorFilter.tint(ColorProvider(
                day = MyAppWidgetGlanceColorScheme.onBackgroundColor,
                night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark
            ))
        )

    }

    @Composable
    private fun TitleView(modifier: GlanceModifier = GlanceModifier){
        val context = LocalContext.current
        Text(
            context.getString(R.string.expenses),
            modifier=modifier,
            style = TextStyle(
                color = ColorProvider(
                    day = MyAppWidgetGlanceColorScheme.onBackgroundColor,
                    night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark
                ),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            ),
        )
    }

    @Composable
    private fun TabsView(modifier: GlanceModifier = GlanceModifier){

    }

    @Composable
    private fun DateView(modifier: GlanceModifier = GlanceModifier){
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DayTextView()
            MonthTextView(GlanceModifier.padding(vertical = 3.dp))
        }
    }

    @Composable
    private fun DayTextView(modifier: GlanceModifier = GlanceModifier){
        val today = DateUtils.getToday()
        val date = today.dayOfMonth.toString()
        Text(
            date,
            modifier=modifier,
            style = TextStyle(
                color = ColorProvider(
                    day = MyAppWidgetGlanceColorScheme.onBackgroundColor,
                    night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            ),
        )

    }

    @Composable
    private fun MonthTextView(modifier: GlanceModifier = GlanceModifier){
        val today = DateUtils.getToday()
        val month = DateUtils.getDisplayMonth(
            today,
            textStyle = java.time.format.TextStyle.SHORT
        )
        Text(
            month,
            modifier=modifier,
            style = TextStyle(
                color = ColorProvider(
                    day = MyAppWidgetGlanceColorScheme.onBackgroundColor,
                    night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            ),
        )

    }

    @Composable
    private fun DividerView(modifier: GlanceModifier = GlanceModifier){

        Column(
            modifier = modifier.height(100.dp).width(1.dp).background(
                ColorProvider(
                    day = MyAppWidgetGlanceColorScheme.onBackgroundColor.copy(alpha = MyAppWidgetGlanceColorScheme.dividerAlpha),
                    night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark.copy(alpha = MyAppWidgetGlanceColorScheme.dividerAlpha)
                )

            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }


    }


    @Composable
    private fun StatisticsView(modifier: GlanceModifier = GlanceModifier){
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            TodayExpensesTextView()
            WeekExpensesTextView(GlanceModifier.padding(top = 8.dp))
            MonthExpensesTextView(GlanceModifier.padding(top = 8.dp))
        }
    }

    @Composable
    private fun TodayExpensesTextView(modifier: GlanceModifier = GlanceModifier){
        val context = LocalContext.current
        val json = currentState(key = TODAY_FINANCIAL_INFO) ?: "0.0"
        val text = "${context.getString(R.string.today_widget_title)} : $json ${Constants.CURRENCY_1_WIDGET}"
        Text(
            text,
            modifier=modifier,
            style = TextStyle(
                color = ColorProvider(
                    day = MyAppWidgetGlanceColorScheme.onBackgroundColor,
                    night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark
                ),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
        )

    }

    @Composable
    private fun WeekExpensesTextView(modifier: GlanceModifier = GlanceModifier){
        val context = LocalContext.current
        val json = currentState(key = WEEK_FINANCIAL_INFO) ?: "0.0"
        val text = "${context.getString(R.string.week_widget_title)} : $json ${Constants.CURRENCY_1_WIDGET}"
        Text(
            text,
            modifier=modifier,
            style = TextStyle(
                color = ColorProvider(
                    day = MyAppWidgetGlanceColorScheme.onBackgroundColor,
                    night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark
                ),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
        )

    }

    @Composable
    private fun MonthExpensesTextView(modifier: GlanceModifier = GlanceModifier){
        val context = LocalContext.current
        val json = currentState(key = MONTH_FINANCIAL_INFO) ?: "0.0"
        val text = "${context.getString(R.string.month_widget_title)} : $json ${Constants.CURRENCY_1_WIDGET}"
        Text(
            text,
            modifier=modifier,
            style = TextStyle(
                color = ColorProvider(
                    day = MyAppWidgetGlanceColorScheme.onBackgroundColor,
                    night = MyAppWidgetGlanceColorScheme.onBackgroundColorDark
                ),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
        )

    }

}

class RefreshAction : ActionCallback {

    companion object {
        private val TAG = RefreshAction::class.java.simpleName
    }
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d(TAG , "onAction()  updating widget...")
        updateAppWidgetState(context, glanceId) { mutablePreferences ->
            mutablePreferences[AppWidget.LOADING_WIDGET_PREF_KEY] = true
            AppWidget().update(context, glanceId)
        }
        val updateAppWidgetWorker = OneTimeWorkRequestBuilder<UpdateAppWidgetJob>().build()
        WorkManager.getInstance(context).enqueue(updateAppWidgetWorker)
        updateAppWidgetState(context, glanceId) { mutablePreferences ->
            mutablePreferences[AppWidget.LOADING_WIDGET_PREF_KEY] = false
            AppWidget().update(context, glanceId)
        }
    }
}

class NavigateWidgetActionCallback : ActionCallback {

    companion object {
        private val TAG = NavigateWidgetActionCallback::class.java.simpleName
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d(TAG, "onAction()  onNavigation clicked")
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        context.startActivity(intent)
    }


}