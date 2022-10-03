package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAdvancedEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class FilterAdvancedModel(
    var id: Int = 0,
    var title: String = "",
    var words: String = "",
    var senderId: Int,
) : Parcelable {
    companion object {
        fun getFilterWordsAsList(searchWord: String?): List<String> {
            return searchWord?.split(",") ?: emptyList()
        }

        fun getFilterWordsAsString(list: List<String>): String {
            return list.joinToString(separator = ",")
        }
    }


}

fun FilterAdvancedModel.toFilterAdvancedEntity() = FilterAdvancedEntity(
    id = id,
    title =  title,
    words = words,
    senderId = senderId)
