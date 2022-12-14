package com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "WordDetectorEntity")
data class WordDetectorEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int=0,
    @ColumnInfo(name = "word")
    var word: String,
    @ColumnInfo(name = "type")
    var type: String,
):Parcelable

fun WordDetectorEntity.toWordDetectorModel()= WordDetectorModel(id, word, type)