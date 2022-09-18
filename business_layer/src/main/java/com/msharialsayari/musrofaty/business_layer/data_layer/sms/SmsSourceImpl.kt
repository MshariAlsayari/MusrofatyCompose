package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import android.net.Uri
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SmsUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SmsSourceImpl @Inject constructor(
    private val wordDetectorRepo: WordDetectorRepo,
    private val senderRepo: SenderRepo
) : SmsDataSource {


    private fun loadAllSms(context: Context, senders:List<String>): List<SmsModel> {
        val lstSms = ArrayList<SmsModel>()
        var objSmsModel: SmsModel
        val message = Uri.parse("content://sms/inbox")
        val cr = context.contentResolver
        val senderFinal = senders.map { it.uppercase() }.toList()
        val projection = arrayOf("_id", "address", "person", "body", "date")
        var whereAddress = "upper(address) IN ("
        senders.forEachIndexed{index,item ->
            whereAddress += "?"
            if (index != senders.lastIndex){
                whereAddress += ","
            }

        }
        whereAddress += ")"


        var c = cr.query(message, projection, whereAddress, senderFinal.toTypedArray(), null)
        val totalSMS = c?.count

        c?.let { cursor ->
            if (cursor.moveToFirst() && totalSMS != null) {
                for (i in 0 until totalSMS) {

                    if (SmsUtils.isValidSms(
                            context,
                            cursor.getString(cursor.getColumnIndexOrThrow("body"))
                        )
                    ) {
                        objSmsModel = SmsModel(id = cursor.getString(cursor.getColumnIndexOrThrow("_id")))
                        objSmsModel.senderName =
                            cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        objSmsModel.body =
                            SmsUtils.clearSms(cursor.getString(cursor.getColumnIndexOrThrow("body")))
                                ?: ""
                        objSmsModel.timestamp =
                            cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong()
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
        val senders = activeSender.map { it.senderName }.toList()
        val allSms = loadAllSms(context,senders)
        val banksSmsList = mutableListOf<SmsModel>()
        val filteredList = allSms.filter {
            it.senderName.isNotEmpty() && senders.find { sender ->
                it.senderName.equals(
                    sender,
                    ignoreCase = true
                )
            } != null
        }
        filteredList.map { smsModel ->
            if (SmsUtils.isAlahliSender(smsModel.senderName)) {
                smsModel.senderName = Constants.ALAHLI_WITH_SAMBA_BANK
            }
            smsModel.senderId = activeSender.find { it.senderName.equals( smsModel.senderName , ignoreCase = true) }?.id!!
            banksSmsList.add(smsModel)

        }
        return banksSmsList
    }
}