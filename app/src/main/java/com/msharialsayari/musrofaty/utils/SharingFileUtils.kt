package com.msharialsayari.musrofaty.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import java.io.File

object SharingFileUtils {

        fun accessFile(context: Context, fileName: String): Uri? {
            val file = File(context.getExternalFilesDir(null), fileName)
            return if (file.exists()) {
                FileProvider.getUriForFile(
                    context,
                    context.packageName.toString() + ".provider",
                    file
                )
            } else {
                null
            }
        }

        fun createSharingIntent(activity: Activity, fileURI: Uri?): Intent {
            return ShareCompat.IntentBuilder.from(activity)
                .setType("application/pdf")
                .setStream(fileURI)
                .setChooserTitle("Select application to share file")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        }


}