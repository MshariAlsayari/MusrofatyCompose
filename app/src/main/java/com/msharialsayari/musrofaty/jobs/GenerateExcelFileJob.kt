package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class GenerateExcelFileJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    val smsRepo: SmsRepo
) :
    CoroutineWorker(appContext, workerParams) {


    private var bankName = ""
    private val scope = CoroutineScope(Dispatchers.IO)


    companion object {
        const val BANK_NAME_EXTRA = "BANK_NAME_EXTRA"
        const val Progress = "Progress"
        const val FILE_GENERATED_EXTRA = "FILE_GENERATED_EXTRA"
        private const val delayDuration = 1L
    }


    override suspend fun doWork(): Result {

        return Result.success()
    }

    private fun createOutputData(isFileGenerated: Boolean): Data {
        return Data.Builder()
            .putBoolean(FILE_GENERATED_EXTRA, isFileGenerated)
            .build()
    }

    private suspend fun getSmsData() = withContext(Dispatchers.IO) {

    }
}