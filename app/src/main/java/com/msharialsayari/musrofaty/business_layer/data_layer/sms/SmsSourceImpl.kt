package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import android.net.Uri
import android.provider.Telephony
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SmsUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SmsSourceImpl @Inject constructor(
    private val senderRepo: SenderRepo,
) : SmsDataSource {


    private fun loadAllSms(context: Context, activeSenders: List<SenderModel>): List<SmsEntity> {
        val lstSms = ArrayList<SmsEntity>()
        val message = Uri.parse("content://sms/inbox")
        val cr = context.contentResolver
        val senders = activeSenders.map { it.senderName.uppercase() }.toList()
        val projection = arrayOf("_id", "address", "person", "body", "date")
        var whereAddress = "upper(address) IN ("
        senders.forEachIndexed { index, item ->
            whereAddress += "?"
            if (index != senders.lastIndex) {
                whereAddress += ","
            }

        }
        whereAddress += ")"


        var c = cr.query(message, projection, whereAddress, senders.toTypedArray(), null)
        val totalSMS = c?.count

        c?.let { cursor ->
            if (cursor.moveToFirst() && totalSMS != null) {
                for (i in 0 until totalSMS) {
                    val smsId = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                    val smsAddress = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                    val senderName =
                        when{
                            SmsUtils.isAlahliSender(smsAddress)    ->   Constants.ALAHLI_WITH_SAMBA_BANK
                            SmsUtils.isArabiBankSender(smsAddress) ->   Constants.ALARABI_BANK
                            else ->    smsAddress
                        }
                    val smsBody =
                        SmsUtils.clearSms(cursor.getString(cursor.getColumnIndexOrThrow("body")))
                            ?: ""
                    val timestamp = cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong()
                    val senderId = activeSenders.find {
                        it.senderName.equals(
                            senderName,
                            ignoreCase = true
                        )
                    }?.id!!

                    if (SmsUtils.isValidSms(smsBody)) {
                        lstSms.add(SmsEntity(
                            id = smsId,
                            body = smsBody,
                            senderName = senderName,
                            senderId = senderId,
                            timestamp = timestamp
                        ))
                    }
                    cursor.moveToNext()

                }
            }
            cursor.close()
        }



        c = null

        return lstSms

    }

    override suspend fun loadBanksSms(context: Context): List<SmsEntity> {
        val activeSender = senderRepo.getSendersModel()
        val allSms = loadAllSms(context, activeSender)
        return allSms
    }

    override suspend fun loadBanksSms(context: Context, senderName: String): List<SmsEntity> {
        val sender = senderRepo.getSenderBySenderName(senderName)
        var allSms = emptyList<SmsEntity>()
        if (sender != null) {
            val senders = mutableListOf<SenderModel>()
            senders.add(sender)
            allSms = loadAllSms(context, senders)
        }
        return allSms
    }

    override suspend fun loadLatestSms(context: Context): SmsEntity? {
        val activeSenders = senderRepo.getSendersModel()
        val returnedValue: SmsEntity?

        val uri: Uri = Uri.parse("content://sms/inbox")
        val cr = context.contentResolver
        val senders = activeSenders.map { it.senderName.uppercase() }.toList()
        val projection = arrayOf("_id", "address", "person", "body", "date")
        var whereAddress = "upper(address) IN ("
        senders.forEachIndexed { index, item ->
            whereAddress += "?"
            if (index != senders.lastIndex) {
                whereAddress += ","
            }

        }
        whereAddress += ")"

        val cursor = cr.query(
            uri,
            projection,
            whereAddress,
            senders.toTypedArray(),
            Telephony.Sms.DATE + " DESC"
        )
        if (cursor != null && cursor.moveToFirst()) {
            val smsId = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
            val smsSenderName = cursor.getString(cursor.getColumnIndexOrThrow("address"))
            val finalSenderName = when{
                SmsUtils.isAlahliSender(smsSenderName)    ->   Constants.ALAHLI_WITH_SAMBA_BANK
                SmsUtils.isArabiBankSender(smsSenderName) ->   Constants.ALARABI_BANK
                else ->    smsSenderName
            }

            val smsBody =
                SmsUtils.clearSms(cursor.getString(cursor.getColumnIndexOrThrow("body"))) ?: ""
            val timestamp = cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong()
            val senderId = activeSenders.find {
                it.senderName.equals(
                    finalSenderName,
                    ignoreCase = true
                )
            }?.id

            returnedValue = if (senderId != null && SmsUtils.isValidSms(smsBody)) {
                SmsEntity(
                    id = smsId,
                    body = smsBody,
                    senderName = finalSenderName,
                    senderId = senderId,
                    timestamp = timestamp
                )
            } else {
                null
            }
        } else {
            returnedValue = null
        }

        cursor?.close()
        return returnedValue

    }


}