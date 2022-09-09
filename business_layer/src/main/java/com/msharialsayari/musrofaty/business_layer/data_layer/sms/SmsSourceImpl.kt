package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import android.net.Uri
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.utils.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SmsSourceImpl @Inject constructor(): SmsDataSource {


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

                    if (SmsUtils.isValidSms(cursor.getString(cursor.getColumnIndexOrThrow("body")))) {
                        objSmsModel             = SmsModel(id = cursor.getString(cursor.getColumnIndexOrThrow("_id")))
                        objSmsModel.senderName  = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        objSmsModel.body        = SmsUtils.clearSms(cursor.getString(cursor.getColumnIndexOrThrow("body")))
                        objSmsModel.timestamp   = DateUtils.getLocalDateTimeByTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong())
                        lstSms.add(objSmsModel)
                    }
                    cursor.moveToNext()

                }
            }
        }


        c=null
        return lstSms

    }

    override suspend fun loadBanksSms(context: Context): List<SmsModel> {
        val allSms = loadAllSms(context)
        val banksSmsList = mutableListOf<SmsModel>()
        val sendersList: List<String> = SharedPreferenceManager.getWordsList(context, WordsType.BANKS_WORDS).toList()
        val filteredList = allSms.filter { it.senderName?.isNotEmpty() == true && sendersList.find { sender-> it.senderName.equals(sender, ignoreCase = true) } != null }
        filteredList.forEach {smsModel->
            smsModel.senderName?.let { bankName ->
                    if (SmsUtils.isAlahliSender(bankName)) {
                        smsModel.senderName = Constants.ALAHLI_WITH_SAMBA_BANK
                    }
                    banksSmsList.add(smsModel)
            }
        }
        return banksSmsList
    }
}