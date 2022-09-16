package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.toSenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SenderRepo @Inject constructor(
    private val dao: SenderDao,
) {

    suspend fun getAllActive():List<SenderModel>{
        val senders = mutableListOf<SenderModel>()
        dao.getAllActive().map { senders.add(it.toSenderModel()) }
        return senders
    }
}