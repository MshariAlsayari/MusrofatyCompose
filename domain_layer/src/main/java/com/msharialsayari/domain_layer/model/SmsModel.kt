package com.msharialsayari.domain_layer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsModel(
    var id: String,
    var senderName: String? = null,
    var dateTime: String? = null,
    var body: String? = null,
    var isDeleted: Boolean? = false
) : Parcelable
