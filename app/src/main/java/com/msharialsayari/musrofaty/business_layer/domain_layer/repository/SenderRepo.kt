package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderWithRelationsModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.toSenderModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toSenderEntity
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SenderRepo @Inject constructor(
    private val dao: SenderDao,
    private val contentRepo: ContentRepo
) {

    suspend fun getSendersModel():List<SenderModel>{
        val senders = mutableListOf<SenderModel>()
        getAllSenders().forEach { senders.add(fillSenderModel( it.toSenderModel())) }
        return senders
    }


     fun getSenders(): Flow<List<SenderEntity>> {
        return dao.getSenders()
    }

    suspend fun getAllSenders(): List<SenderEntity> {
        return dao.getAll()
    }

    suspend fun getSenderById(senderId:Int):SenderModel?{
        val model = dao.getSenderById(senderId)?.toSenderModel()
        if (model != null)
        return fillSenderModel(model)
        return null
    }

    suspend fun getSenderBySenderName(senderName:String):SenderModel?{
        val model = dao.getSenderBySenderName(senderName)?.toSenderModel()
        if (model != null)
            return fillSenderModel(model)
        return null
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


    suspend fun changeSenderIcon(senderId:Int, iconPath:String){
        dao.updateIcon(senderId, iconPath)
    }




    private suspend fun fillSenderModel(senderModel: SenderModel): SenderModel {
        senderModel.content = contentRepo.getContentById(senderModel.contentId)
        return senderModel
    }

    suspend fun insert(vararg model: SenderModel){
        val senders = model.toList().filter { dao.getSenderBySenderName(it.senderName) == null }.map { it.toSenderEntity() }.toList()
        dao.insert(*senders.toTypedArray())
    }

    suspend fun delete(senderId: Int){
        dao.delete(senderId)
    }
}