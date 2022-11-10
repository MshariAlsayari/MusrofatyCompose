package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.SendersKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.*
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.WordsType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val smsRepo: SmsRepo,
    private val wordDetectorRepo: WordDetectorRepo,
    private val contentRepo: ContentRepo,
    private val senderRepo: SenderRepo,
    private val filtersRepo: FilterRepo,
    private val categoryRepo: CategoryRepo,
    @ApplicationContext val context: Context
) : ViewModel() {


     private val _uiState = MutableStateFlow(SplashUiState())
     val uiState  : StateFlow<SplashUiState> = _uiState

init {
    initData()
}

    private fun initData(){
        viewModelScope.launch {
            if (SharedPreferenceManager.isFirstLunch(context)) {
                initIncomesWords()
                initExpensesWords()
                initCurrencyWords()
                initContent()
                initSenders()
                initFilters()
                initCategories()
                insertSms()
                SharedPreferenceManager.setFirstLunch(context)
            }else{
                insertSms()
            }




        }



    }

    private  fun initContent(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val defaultContent = ContentModel.getDefaultContent()
            contentRepo.insert(*defaultContent.toTypedArray())
            _uiState.update {
                it.copy(isLoading = false)
            }
        }

    }


    private  fun initIncomesWords(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            if (SharedPreferenceManager.isDefaultSmsWordsListChanged(
                    context,
                    WordsType.INCOME_WORDS
                )
            ) {
                val words = SharedPreferenceManager.getWordsList(context, WordsType.INCOME_WORDS)
                val incomes = words.map {
                    WordDetectorModel(
                        word = it,
                        type = WordDetectorType.INCOME_WORDS.name
                    )
                }.toList()
                wordDetectorRepo.insert(incomes)
            } else {
                val incomes = Constants.listIncomeWords.map {
                    WordDetectorModel(
                        word = it,
                        type = WordDetectorType.INCOME_WORDS.name
                    )
                }.toList()
                wordDetectorRepo.insert(incomes)
            }

            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private  fun initExpensesWords(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            if (SharedPreferenceManager.isDefaultSmsWordsListChanged(
                    context,
                    WordsType.EXPENSES_WORDS
                )
            ) {
                val words = SharedPreferenceManager.getWordsList(context, WordsType.EXPENSES_WORDS)
                val expenses = words.map {
                    WordDetectorModel(
                        word = it,
                        type = WordDetectorType.EXPENSES_WORDS.name
                    )
                }.toList()
                wordDetectorRepo.insert(expenses)
            } else {
                val expenses = Constants.listExpenseWords.map {
                    WordDetectorModel(
                        word = it,
                        type = WordDetectorType.EXPENSES_WORDS.name
                    )
                }.toList()
                wordDetectorRepo.insert(expenses)
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }

    }

    private  fun initCurrencyWords(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            if (SharedPreferenceManager.isDefaultSmsWordsListChanged(
                    context,
                    WordsType.CURRENCY_WORDS
                )
            ) {
                val words = SharedPreferenceManager.getWordsList(context, WordsType.CURRENCY_WORDS)
                val currency = words.map {
                    WordDetectorModel(
                        word = it,
                        type = WordDetectorType.CURRENCY_WORDS.name
                    )
                }.toList()
                wordDetectorRepo.insert(currency)
            } else {
                val currency = Constants.listCurrencyWords.map {
                    WordDetectorModel(
                        word = it,
                        type = WordDetectorType.CURRENCY_WORDS.name
                    )
                }.toList()
                wordDetectorRepo.insert(currency)
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }


    }

    private fun initSenders(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val sendersContent = contentRepo.getContentByKey(ContentKey.SENDERS.name)
            val banksSender = sendersContent.find { it.valueAr == SendersKey.BANKS.valueAr }
            val servicesSender = sendersContent.find { it.valueAr == SendersKey.SERVICES.valueAr }
            val digitalWalletSender =
                sendersContent.find { it.valueAr == SendersKey.DIGITALWALLET.valueAr }

            if (SharedPreferenceManager.isDefaultSmsWordsListChanged(
                    context,
                    WordsType.BANKS_WORDS
                )
            ) {
                val words = SharedPreferenceManager.getWordsList(context, WordsType.BANKS_WORDS)
                val senders = words.map {
                    SenderModel(
                        contentId = banksSender?.id ?: 0,
                        senderName = it,
                        displayNameAr = it,
                        displayNameEn = it
                    )
                }.toList()
                senderRepo.insert(*senders.toTypedArray())
            } else {


                val senders = SenderModel.getDefaultSendersModel(
                    bankContentId = banksSender?.id ?: 0,
                    servicesContentId = servicesSender?.id ?: 0,
                    digitalWalletContentId = digitalWalletSender?.id ?: 0

                )
                senderRepo.insert(*senders.toTypedArray())

            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }

    }

    private  fun initFilters(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            filtersRepo.migrateForFilters()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private  fun initCategories(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            categoryRepo.insertDefaultCategoryList()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

     private fun insertSms(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            smsRepo.insert()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }

    }




     data class SplashUiState(
        var isLoading:Boolean = false
    )

}