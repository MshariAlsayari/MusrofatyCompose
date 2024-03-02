package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAmountEntity
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

data class FilterAmountModel(
    var amountId: Int = 0,
    var filterId: Int = 0,
    var amount: String = "0.0",
    var amountOperator: AmountOperators,
)

data class FilterWithWordsModel(
    val filter: FilterModel,
    val words: List<FilterWordModel> = emptyList(),
    val amountFilter: FilterAmountModel? = null
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
    amountFilter = amountFilter?.toFilterAmountEntity()
)

fun FilterAmountModel.toFilterAmountEntity() = FilterAmountEntity(
    amountId=amountId,
    filterId=filterId,
    amount=amount,
    amountOperator = amountOperator.name
)



enum class LogicOperators(val value:String){
    AND("&&"),OR("||")
}

enum class AmountOperators(val valueString:Int){
    EQUAL_OR_MORE(R.string.pref_managment_sms_tool_summary),
    EQUAL_OR_LESS(R.string.pref_managment_sms_tool_summary),
    EQUAL(R.string.pref_managment_sms_tool_summary)
}