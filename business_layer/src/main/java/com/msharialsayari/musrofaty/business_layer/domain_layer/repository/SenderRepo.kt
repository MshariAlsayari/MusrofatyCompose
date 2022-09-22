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

    suspend fun getSenderById(senderId:Int):SenderModel{
        val model =  dao.getSenderById(senderId).toSenderModel()
        return fillSenderModel(model)
    }

    suspend fun activeSender(senderId:Int, active:Boolean){
        dao.activeSender(senderId,active)
    }

    suspend fun pinSender(senderId:Int, pin:Boolean){
        dao.removePinFromAll()
        dao.pinSender(senderId,pin)
    }

    suspend fun changeDisplayName(senderId:Int, isArabic:Boolean, name:String){
      if (isArabic){
          dao.updateArabicDisplayName(senderId, name)
      }else{
          dao.updateEnglishDisplayName(senderId, name)
      }
    }

    suspend fun changeCategory(senderId:Int, categoryId:Int){
        dao.updateCategory(senderId, categoryId)
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