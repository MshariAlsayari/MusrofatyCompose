package com.msharialsayari.musrofaty

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExcelModel(
    var smsList:  List<SmsModel> = listOf(),
) : Parcelable