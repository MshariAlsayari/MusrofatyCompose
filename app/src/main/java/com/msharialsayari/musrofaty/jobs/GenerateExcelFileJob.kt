package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.msharialsayari.musrofaty.excei.ExcelModel
import com.msharialsayari.musrofaty.excei.ExcelUtils
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsListUseCase
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class GenerateExcelFileJob @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val getAllSmsUseCase: GetSmsListUseCase
) : CoroutineWorker(appContext, workerParams) {


    private var senderId = 0
    private var filterTimeId = 0
    private var filterWord = ""
    private var startDate = 0L
    private var endDate = 0L




    companion object {

        const val Progress = "Progress"
        const val FILE_GENERATED_EXTRA = "FILE_GENERATED_EXTRA"


        const val SENDER_ID = "SENDER_ID"
        const val FILTER_TIME_OPTION = "FILTER_TIME_OPTION"
        const val FILTER_WORD = "FILTER_WORD"
        const val START_TIME = "START_TIME"
        const val END_TIME = "END_TIME"

    }


    override suspend fun doWork(): Result {
        senderId     = inputData.getInt(SENDER_ID, 0)
        filterTimeId = inputData.getInt(FILTER_TIME_OPTION, 0)
        filterWord   = inputData.getString(FILTER_WORD)?:""
        startDate    = inputData.getLong(START_TIME,0L)
        endDate      = inputData.getLong(END_TIME,0L)
        val firstUpdate = workDataOf(Progress to 0)
        val lastUpdate = workDataOf(Progress to 100)
        setProgress(firstUpdate)
        val smsResult = getSmsData()
        val excelModel = ExcelModel(smsList = smsResult,)
        val isGenerated = ExcelUtils(
            appContext,
            Constants.EXCEL_FILE_NAME
        ).exportDataIntoWorkbook(excelModel)
        setProgress(lastUpdate)
        return Result.success(workDataOf(FILE_GENERATED_EXTRA to isGenerated))
    }


    private suspend fun getSmsData() = withContext(Dispatchers.IO) {
        getAllSmsUseCase.invoke(
            senderId = senderId,
            filterOption =DateUtils.FilterOption.getFilterOption(filterTimeId) ,
            query = filterWord,
            startDate = startDate,
            endDate= endDate)
    }
}