package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderWithRelationsModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.toSenderModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toSenderEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SenderRepo @Inject constructor(
    private val dao: SenderDao,
    private val contentRepo: ContentRepo
) {

    suspend fun getAllActive():List<SenderModel>{
        val senders = mutableListOf<SenderModel>()
        dao.getAllActive().forEach { senders.add(fillSenderModel( it.toSenderModel())) }
        return senders
    }

    suspend fun activeSender(senderName:String, active:Boolean){
        dao.activeSender(senderName,active)
    }

    suspend fun pinSender(senderName:String, pin:Boolean){
        dao.removePinFromAll()
        dao.pinSender(senderName,pin)
    }



    suspend fun getAllSendersWithSms(): List<SenderWithRelationsModel>{
        val sendersWithSms = mutableListOf<SenderWithRelationsModel>()
          dao.getAllSendersWithSms().forEach {
              val model = SenderWithRelationsModel(sender =  fillSenderModel( it.sender.toSenderModel()), sms = it.sms.map { it.toSmsModel() }.toList())
              sendersWithSms.add(model)

        }

        return sendersWithSms
    }

    private suspend fun fillSenderModel(senderModel: SenderModel): SenderModel {
        senderModel.content = contentRepo.getContentById(senderModel.contentId)
        return senderModel
    }

    suspend fun insert(vararg model: SenderModel){
        val senders = model.toList().map { it.toSenderEntity() }.toList()
        dao.insert(*senders.toTypedArray())

    }
}