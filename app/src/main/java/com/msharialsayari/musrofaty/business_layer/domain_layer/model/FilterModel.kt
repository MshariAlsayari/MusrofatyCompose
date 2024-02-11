package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterWithWordsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterWordEntity

data class FilterModel (
    var id: Int = 0,
    var title: String = "",
    var senderId: Int,
)

data class FilterWordModel(
    var wordId: Int = 0,
    var filterId: Int = 0,
    var word: String = "",
    var logicOperator: LogicOperators,
)

data class FilterWithWordsModel(
    val filter: FilterModel,
    val words: List<FilterWordModel> = emptyList()
)

fun FilterModel.toFilterEntity() = FilterEntity(
    id = id,
    title =  title,
    senderId = senderId)


fun FilterWordModel.toFilterWordEntity() = FilterWordEntity(
    wordId=wordId,
    filterId=filterId,
    word=word,
    logicOperator=logicOperator.name
)

fun FilterWithWordsModel.toFilterWordEntity() = FilterWithWordsEntity(
    filter=filter.toFilterEntity(),
    words = words.map { it.toFilterWordEntity() },
)



enum class LogicOperators(val value:String){
    AND("&&"),OR("||")
}