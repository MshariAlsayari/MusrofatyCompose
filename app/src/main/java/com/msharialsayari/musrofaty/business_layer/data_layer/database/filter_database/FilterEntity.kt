package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWithWordsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "FilterEntity")
class FilterEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "senderId")
    var senderId: Int,
) : Parcelable


@Parcelize
@Entity(tableName = "FilterWordEntity")
class FilterWordEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wordId")
    var wordId: Int = 0,
    @ColumnInfo(name = "filterId")
    var filterId: Int = 0,
    @ColumnInfo(name = "word")
    var word: String = "",
    @ColumnInfo(name = "logicOperator")
    var logicOperator: String = "",
) : Parcelable


@Parcelize
data class FilterWithWordsEntity(
    @Embedded val filter: FilterEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "filterId"
    )
    val words: List<FilterWordEntity> = emptyList()
):Parcelable


fun FilterEntity.toFilterModel() = FilterModel(
    id = id,
    title =  title,
    senderId = senderId)


fun FilterWordEntity.toFilterWordModel() = FilterWordModel(
    wordId=wordId,
    filterId=filterId,
    word=word,
    logicOperator= enumValueOf(logicOperator)
)

fun FilterWithWordsEntity.toFilterWithWordsModel() = FilterWithWordsModel(
    filter=filter.toFilterModel(),
    words = words.map { it.toFilterWordModel() },
)
