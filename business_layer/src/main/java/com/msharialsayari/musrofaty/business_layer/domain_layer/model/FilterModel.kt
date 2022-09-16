package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class FilterModel(
    var id: Int = 0,
    var title: String = "",
    var words: String = "",
    var smsType: String = "",
    var filterOption: String = "",
    var senderName: String = "",
    var dateFrom: Long = 0,
    var dateTo: Long = 0
) : Parcelable {
    var isSelected: Boolean = false
    companion object {
        fun getFilterWordsAsList(searchWord: String?): List<String> {
            return searchWord?.split(",") ?: emptyList()
        }

        fun getFilterWordsAsString(list: List<String>): String {
            return list.joinToString(separator = ",")
        }
    }


}

fun FilterModel.toFilterEntity() = FilterEntity(
    id = id,
    title =  title,
    words = words,
    smsType = smsType,
    filterOption=filterOption,
    senderName = senderName,
    dateFrom =  dateFrom,
    dateTo =  dateTo)
