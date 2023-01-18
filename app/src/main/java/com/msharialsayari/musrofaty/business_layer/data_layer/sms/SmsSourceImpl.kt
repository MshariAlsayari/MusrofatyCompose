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
    private val senderRepo: SenderRepo,
) : SmsDataSource {


    private suspend fun loadAllSms(context: Context, activeSenders: List<SenderModel>): List<SmsModel> {
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
                    val smsId      = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                    val senderName =
                        if (SmsUtils.isAlahliSender(cursor.getString(cursor.getColumnIndexOrThrow("address")))) {
                            Constants.ALAHLI_WITH_SAMBA_BANK
                        } else {
                            cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        }
                    val smsBody    = SmsUtils.clearSms(cursor.getString(cursor.getColumnIndexOrThrow("body"))) ?: ""
                    val timestamp  = cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong()
                    val senderId   = activeSenders.find { it.senderName.equals( senderName , ignoreCase = true) }?.id!!

                    if (SmsUtils.isValidSms(smsBody)) {
                        objSmsModel            = SmsModel(id = smsId)
                        objSmsModel.senderName = senderName
                        objSmsModel.body       = smsBody
                        objSmsModel.timestamp  = timestamp
                        objSmsModel.senderId   = senderId
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