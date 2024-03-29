package com.msharialsayari.musrofaty


import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
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

    @JvmStatic
    fun shareText(text: String?, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT")
        intent.putExtra(Intent.EXTRA_TEXT, text?:"")
        context.startActivity(
            Intent.createChooser(intent, context.getString(R.string.share_by))
        )
    }

    @JvmStatic
    fun getScreenSize(activity: Activity): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return displayMetrics
    }





}