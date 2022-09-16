package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.toWordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toWordDetectorEntity
import com.msharialsayari.musrofaty.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordDetectorRepo @Inject constructor(
    private val dao: WordDetectorDao,
    @ApplicationContext val context: Context
) {


    suspend fun getAll(type:WordDetectorType):List<WordDetectorModel> {
        val returnedList: MutableList<WordDetectorModel> = mutableListOf()
        dao.getAll(type.name).map {
            returnedList.add(it.toWordDetectorModel())
        }
        return returnedList
    }

    suspend fun getAllActive(type:WordDetectorType):List<WordDetectorModel> {
        val returnedList: MutableList<WordDetectorModel> = mutableListOf()
        dao.getAllActive(type.name).map {
            returnedList.add(it.toWordDetectorModel())
        }
        return returnedList
    }

    suspend fun insert(vararg list: WordDetectorModel) {
        val words: MutableList<WordDetectorEntity> = mutableListOf()
        list.toList().map {
            words.add(it.toWordDetectorEntity())
        }
        dao.insert(*words.toTypedArray())
    }


    suspend fun insertDefault() {
        val currency = Constants.listCurrencyWords.map { WordDetectorModel(word = it, type = WordDetectorType.CURRENCY_WORDS.name, isActive = true) }.toList()
        val expenses = Constants.listExpenseWords.map { WordDetectorModel(word = it, type = WordDetectorType.EXPENSES_WORDS.name, isActive = true) }.toList()
        val incomes = Constants.listIncomeWords.map { WordDetectorModel(word = it, type = WordDetectorType.INCOME_WORDS.name, isActive = true) }.toList()
        val senders = Constants.listOfSenders.map { WordDetectorModel(word = it, type = WordDetectorType.SENDERS_WORDS.name, isActive = true) }.toList()
        insert(*currency.toTypedArray())
        insert(*expenses.toTypedArray())
        insert(*incomes.toTypedArray())
        insert(*senders.toTypedArray())
    }


    suspend fun update( item: WordDetectorModel) {
        dao.update(item.toWordDetectorEntity())
    }
}