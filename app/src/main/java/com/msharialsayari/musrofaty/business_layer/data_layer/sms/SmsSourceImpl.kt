package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import android.net.Uri
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SmsUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SmsSourceImpl @Inject constructor(
    private val senderRepo: SenderRepo
) : SmsDataSource {


    private fun loadAllSms(context: Context, activeSenders: List<SenderModel>): List<SmsModel> {
        val lstSms = ArrayList<SmsModel>()
        var objSmsModel: SmsModel
        val message = Uri.parse("content://sms/inbox")
        val cr = context.contentResolver
        val senders = activeSenders.map { it.senderName.uppercase() }.toList()
        val projection = arrayOf("_id", "address", "person", "body", "date")
        var whereAddress = "upper(address) IN ("
        senders.forEachIndexed{index,item ->
            whereAddress += "?"
            if (index != senders.lastIndex){
                whereAddress += ","
            }

        }
        whereAddress += ")"


        var c = cr.query(message, projection, whereAddress, senders.toTypedArray(), null)
        val totalSMS = c?.count

        c?.let { cursor ->
            if (cursor.moveToFirst() && totalSMS != null) {
                for (i in 0 until totalSMS) {

                    if (SmsUtils.isValidSms(
                            cursor.getString(cursor.getColumnIndexOrThrow("body"))
                        )
                    ) {
                        objSmsModel = SmsModel(id = cursor.getString(cursor.getColumnIndexOrThrow("_id")))
                        if (SmsUtils.isAlahliSender(cursor.getString(cursor.getColumnIndexOrThrow("address")))) {
                            objSmsModel.senderName = Constants.ALAHLI_WITH_SAMBA_BANK
                        }else {
                            objSmsModel.senderName = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        }
                        objSmsModel.body = SmsUtils.clearSms(cursor.getString(cursor.getColumnIndexOrThrow("body"))) ?: ""
                        objSmsModel.timestamp = cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong()
                        objSmsModel.senderId = activeSenders.find { it.senderName.equals( objSmsModel.senderName , ignoreCase = true) }?.id!!
                        lstSms.add(objSmsModel)
                    }
                    cursor.moveToNext()

                }
            }
        }


        c = null
        return lstSms

    }

    override suspend fun loadBanksSms(context: Context): List<SmsModel> {
        val activeSender = senderRepo.getAllActive()
        val allSms = loadAllSms(context,activeSender)
        return allSms
    }

    override suspend fun loadBanksSms(context: Context, senderName: String): List<SmsModel> {
        val sender = senderRepo.getSenderBySenderName(senderName)
        var allSms = emptyList<SmsModel>()
        if (sender != null) {
            val senders = mutableListOf<SenderModel>()
             senders.add(sender)
             allSms = loadAllSms(context, senders)
        }
        return allSms
    }
}