package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InsertLatestSmsUseCase @Inject constructor(
    private val smsRepo: SmsRepo,
    @ApplicationContext val context: Context
    ) {

    suspend operator fun invoke(){
        return smsRepo.insertLatestSms()
    }
}