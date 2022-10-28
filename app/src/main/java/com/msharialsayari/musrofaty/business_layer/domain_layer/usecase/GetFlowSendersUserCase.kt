package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFlowSendersUserCase @Inject constructor(
    private val senderRepo: SenderRepo
) {

     operator fun invoke(): Flow<List<SenderEntity>> {
        return senderRepo.getSenders()

    }
}