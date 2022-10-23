package com.msharialsayari.musrofaty


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.utils.DateUtils

object Utils {


    @JvmStatic
    fun getMappedSmsByMonth(
        smsList: List<SmsModel>,
        pattern: String
    ): Map<String, List<SmsModel>> {
        return smsList.groupBy {
         DateUtils.getDateByTimestamp(it.timestamp, pattern)!!
        }
    }


    @JvmStatic
    fun copyToClipboard(text: String?, context: Context) {
        val clipboard: ClipboardManager = context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", text ?: "")
        clipboard.setPrimaryClip(clip)
    }
}