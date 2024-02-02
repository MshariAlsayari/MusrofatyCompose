package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetWordDetectorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmsAnalysisViewModel @Inject constructor(
    private val getWordDetectorUseCase: GetWordDetectorUseCase,
    private val addWordDetectorUseCase: AddWordDetectorUseCase ,
    private val deleteWordDetectorUseCase: DeleteWordDetectorUseCase
    ):ViewModel() {

    private val _uiState = MutableStateFlow(SmsAnalysisUIState())
    val uiState: StateFlow<SmsAnalysisUIState> = _uiState

    init {
        getWordDetectors()
    }


    private fun getWordDetectors(){
        viewModelScope.launch {
            val expensesResult = getWordDetectorUseCase.invoke(WordDetectorType.EXPENSES_PURCHASES_WORDS)
            val expensesOutgoingResult = getWordDetectorUseCase.invoke(WordDetectorType.EXPENSES_OUTGOING_TRANSFER_WORDS)
            val expensesPayBillsResult = getWordDetectorUseCase.invoke(WordDetectorType.EXPENSES_PAY_BILLS_WORDS)
            val expensesWithdrawalATMResult = getWordDetectorUseCase.invoke(WordDetectorType.WITHDRAWAL_ATM_WORDS)
            val incomesResult = getWordDetectorUseCase.invoke(WordDetectorType.INCOME_WORDS)
            val amountsResult = getWordDetectorUseCase.invoke(WordDetectorType.AMOUNT_WORDS)
            val storesResult = getWordDetectorUseCase.invoke(WordDetectorType.STORE_WORDS)
            val currencyResult = getWordDetectorUseCase.invoke(WordDetectorType.CURRENCY_WORDS)
            _uiState.update {
                it.copy(
                    currencyList = currencyResult,
                    expensesPurchasesList = expensesResult,
                    expensesOutgoingTransferList = expensesOutgoingResult,
                    expensesPayBillsList = expensesPayBillsResult,
                    expensesWithdrawalATMList = expensesWithdrawalATMResult,
                    incomesList = incomesResult,
                    amountsList = amountsResult,
                    storesList = storesResult
                )
            }
        }

    }

    fun addWordDetector(value:String,type: WordDetectorType){
        viewModelScope.launch {
            val model = WordDetectorModel(word = value, type = type.name)
            addWordDetectorUseCase.invoke(model)
        }
    }

    fun deleteWordDetector(id:Int){
        viewModelScope.launch {
            deleteWordDetectorUseCase.invoke(id)
        }
    }



}