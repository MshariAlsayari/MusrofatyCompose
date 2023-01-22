package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.SendersKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.ContentRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitSendersJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val contentRepo: ContentRepo,
    private val senderRepo: SenderRepo,
): CoroutineWorker(appContext, workerParams){
    override suspend fun doWork(): Result {
        initSenders()
        return Result.success()
    }


    private suspend fun initSenders() {
        val sendersContent = contentRepo.getContentByKey(ContentKey.SENDERS.name)
        val banksSender = sendersContent.find { it.valueAr == SendersKey.BANKS.valueAr }
        val servicesSender = sendersContent.find { it.valueAr == SendersKey.SERVICES.valueAr }
        val digitalWalletSender = sendersContent.find { it.valueAr == SendersKey.DIGITALWALLET.valueAr }
        val tdawelSender = sendersContent.find { it.valueAr == SendersKey.TDAWEL.valueAr }
        val senders = SenderModel.getDefaultSendersModel(
            bankContentId = banksSender?.id ?: 0,
            servicesContentId = servicesSender?.id ?: 0,
            digitalWalletContentId = digitalWalletSender?.id ?: 0,
            tdawelSenderId = tdawelSender?.id ?: 0
        )
        senderRepo.insert(*senders.toTypedArray())

    }
}