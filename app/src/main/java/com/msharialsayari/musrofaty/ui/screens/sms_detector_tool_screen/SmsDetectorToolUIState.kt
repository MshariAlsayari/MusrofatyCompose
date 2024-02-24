package com.msharialsayari.musrofaty.ui.screens.sms_detector_tool_screen

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.utils.enums.SmsType

data class SmsDetectorToolUIState(
    val sender:SenderModel? = null,
    val storeName:String = "",
    val amount:String = "",
    val currency:String = "",
    val smsType: SmsType = SmsType.NOTHING
)