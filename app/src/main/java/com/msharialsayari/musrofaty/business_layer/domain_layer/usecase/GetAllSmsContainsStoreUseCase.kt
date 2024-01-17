package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllSmsContainsStoreUseCase @Inject constructor(private val smsRepo: SmsRepo) {

    suspend operator fun invoke(storeName:String = ""): List<SmsModel> {
        return smsRepo.getAllSmsContainsStore(storeName)
    }
}