package com.msharialsayari.musrofaty.business_layer.data_layer.database.content_database

import android.os.Parcelable
import androidx.room.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ContentEntity")
class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "contentKey")
    var contentKey: String = "",
    @ColumnInfo(name = "value_ar")
    var valueAr: String? = null,
    @ColumnInfo(name = "value_en")
    var valueEn: String? = null,

) : Parcelable

@Parcelize
data class ContentWithRelations(
    @Embedded val contentEntity: ContentEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "contentId"
    )
    val sender: SenderEntity
):Parcelable


fun ContentEntity.toContentModel() = ContentModel(id,contentKey, valueAr, valueEn)