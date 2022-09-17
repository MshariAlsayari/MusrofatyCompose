package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toSenderEntity
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

    suspend fun getAllSendersWithSms(): List<SenderWithRelationsModel>{
        val sendersWithSms = mutableListOf<SenderWithRelationsModel>()
          dao.getAllSendersWithSms().forEach {
              val model = SenderWithRelationsModel(sender =   it.sender.toSenderModel(), sms = it.sms.map { it.toSmsModel() }.toList())
              sendersWithSms.add(model)

        }

        return sendersWithSms
    }

    suspend fun insert(vararg model: SenderModel){
        val senders = model.toList().map { it.toSenderEntity() }.toList()
        dao.insert(*senders.toTypedArray())

    }
}