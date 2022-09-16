package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordDetectorModel(
    var id: Int=0,
    var word: String,
    var type: String,
    var isActive: Boolean=true,
) : Parcelable

fun WordDetectorModel.toWordDetectorEntity()= WordDetectorEntity(id, word, type, isActive)