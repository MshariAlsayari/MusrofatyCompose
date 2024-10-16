package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.StringsUtils
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import com.msharialsayari.musrofaty.widgets.AppWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateAppWidgetJob  @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val getAllSmsUseCase: GetSmsModelListUseCase,
    private val getFinancialStatisticsUseCase: GetFinancialStatisticsUseCase,
): CoroutineWorker(appContext, workerParams){


    private var glanceId:GlanceId? = null

    companion object {
         val TAG = UpdateAppWidgetJob::class.java.simpleName
    }

    override suspend fun doWork(): Result {
        glanceId = GlanceAppWidgetManager(appContext).getGlanceIds(AppWidget::class.java).firstOrNull()
        Log.d(TAG, "doWork() updating the app widget glanceId: $glanceId")
        glanceId?.let {glanceId->
            initInsertSmsJob(appContext)
            val todaySmsList = getAllSmsUseCase.invoke(
                filterOption = DateUtils.FilterOption.TODAY,
                isDeleted = false
            )
            val weekSmsList = getAllSmsUseCase.invoke(
                filterOption = DateUtils.FilterOption.WEEK,
                isDeleted = false
            )
            val monthSmsList = getAllSmsUseCase.invoke(
                filterOption = DateUtils.FilterOption.MONTH,
                isDeleted = false
            )

            val todayFinResult = getFinancialStatisticsUseCase(todaySmsList)
            val weekFinResult  = getFinancialStatisticsUseCase(weekSmsList)
            val monthFinResult = getFinancialStatisticsUseCase(monthSmsList)

            updateAppWidgetState(appContext, glanceId) { mutablePreferences ->
                val todayPref = mutablePreferences[AppWidget.TODAY_FINANCIAL_INFO]
                val weekPref  = mutablePreferences[AppWidget.WEEK_FINANCIAL_INFO]
                val monthPref = mutablePreferences[AppWidget.MONTH_FINANCIAL_INFO]

                val today = DateUtils.getToday()
                val timestamp = DateUtils.toMilliSecond(today)
                mutablePreferences[AppWidget.DATE_INFO] = timestamp

                if (todayPref != null) {
                    val json = todayFinResult.getOrDefault(Constants.CURRENCY_1, FinancialStatistics())
                    val formattedNumber = StringsUtils.prettyCount(json.expenses.toLong())
                    mutablePreferences[AppWidget.TODAY_FINANCIAL_INFO] = formattedNumber
                } else {
                    mutablePreferences[AppWidget.TODAY_FINANCIAL_INFO] = "0.0"
                }

                if (weekPref != null) {
                    val json = weekFinResult.getOrDefault(Constants.CURRENCY_1, FinancialStatistics())
                    val formattedNumber = StringsUtils.prettyCount(json.expenses.toLong())
                    mutablePreferences[AppWidget.WEEK_FINANCIAL_INFO] = formattedNumber
                } else {
                    mutablePreferences[AppWidget.WEEK_FINANCIAL_INFO] = "0.0"
                }

                if (monthPref != null) {
                    val json = monthFinResult.getOrDefault(Constants.CURRENCY_1, FinancialStatistics())
                    val formattedNumber = StringsUtils.prettyCount(json.expenses.toLong())
                    mutablePreferences[AppWidget.MONTH_FINANCIAL_INFO] = formattedNumber
                } else {
                    mutablePreferences[AppWidget.MONTH_FINANCIAL_INFO] = "0.0"
                }

                Log.d(TAG, "doWork() finish updating the app widget glanceId: $glanceId")
                AppWidget().update(appContext, glanceId)
            }
        }
        return Result.success()
    }

    private fun initInsertSmsJob(context: Context){
        val initStoresWorker = OneTimeWorkRequestBuilder<InsertSmsJob>().build()
        WorkManager.getInstance(context).enqueue(initStoresWorker)
    }
}