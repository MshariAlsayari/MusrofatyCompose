package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObservingSmsListByIdsUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

    operator fun invoke(
        ids:List<String>
    ): Flow<List<SmsModel>> {
        return smsRepo.observingAllSmsModelByIds(ids)
    }
}

