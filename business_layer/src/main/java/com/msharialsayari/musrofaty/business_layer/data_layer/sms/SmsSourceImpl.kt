package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import android.net.Uri
import android.util.Log
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SmsUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SmsSourceImpl @Inject constructor(private val wordDetectorRepo: WordDetectorRepo) : SmsDataSource {


    private fun loadAllSms(context: Context): List<SmsModel> {
        val lstSms = ArrayList<SmsModel>()
        var objSmsModel: SmsModel
        val message = Uri.parse("content://sms/")
        val cr = context.contentResolver

        var c = cr.query(message, null, null, null, null)
        val totalSMS = c?.count

        c?.let { cursor ->
            if (cursor.moveToFirst() && totalSMS != null) {
                for (i in 0 until totalSMS) {

                    if (SmsUtils.isValidSms(
                            context,
                            cursor.getString(cursor.getColumnIndexOrThrow("body"))
                        )
                    ) {
                        objSmsModel =
                            SmsModel(id = cursor.getString(cursor.getColumnIndexOrThrow("_id")))
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

    override suspend fun loadBanksSms(context: Context, ): List<SmsModel> {
        val senders = wordDetectorRepo.getAllActive(WordDetectorType.SENDERS_WORDS).map { it.word }.toList()
        val allSms = loadAllSms(context)
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
            banksSmsList.add(smsModel)

        }
        Log.i("Mshari",banksSmsList.size.toString() )
        return banksSmsList
    }
}