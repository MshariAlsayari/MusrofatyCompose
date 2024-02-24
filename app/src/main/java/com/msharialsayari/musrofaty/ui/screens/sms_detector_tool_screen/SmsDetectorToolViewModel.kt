package com.msharialsayari.musrofaty.ui.screens.sms_detector_tool_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmsDetectorToolViewModel @Inject constructor(
    private val wordDetectorRepo: WordDetectorRepo,
) : ViewModel() {


    private val _uiState = MutableStateFlow(SmsDetectorToolUIState())
    val uiState: StateFlow<SmsDetectorToolUIState> = _uiState

    fun onSmsTextFieldChanged(text: String) {
        viewModelScope.launch {
            val smsType = getSmsType(text)
            val currency = getSmsCurrency(text)
            val amount = getAmount(text)
            val storeName = getStoreName(text,smsType)

            _uiState.update {
                it.copy(
                    smsType   = smsType,
                    amount    = amount.toString() ,
                    currency  = currency ,
                    storeName = storeName,
                )
            }
        }




    }


    private suspend fun getSmsCurrency(body: String): String {
        val currencyWord = wordDetectorRepo.getAll(WordDetectorType.CURRENCY_WORDS).map { it.word }
        val amountWord = wordDetectorRepo.getAll(WordDetectorType.AMOUNT_WORDS).map { it.word }
        return SmsUtils.getCurrency(body, currencyList = currencyWord, amountWord)
    }

    private suspend fun getAmount(body: String): Double {
        val currencyWord = wordDetectorRepo.getAll(WordDetectorType.CURRENCY_WORDS).map { it.word }
        val amountWord = wordDetectorRepo.getAll(WordDetectorType.AMOUNT_WORDS).map { it.word }
        return SmsUtils.extractAmount(body, currencyList = currencyWord,amountWord)
    }

    private suspend fun getStoreName(body: String,smsType: SmsType): String {
        val storesWord = wordDetectorRepo.getAll(WordDetectorType.STORE_WORDS).map { it.word }
        return SmsUtils.getStoreName(body,smsType,storesWord)
    }

    private suspend fun getSmsType(body: String,senderName: String = Constants.ALRAJHI_BANK): SmsType {
        val expensesPurchasesWord = wordDetectorRepo.getAll(WordDetectorType.EXPENSES_PURCHASES_WORDS).map { it.word }
        val expensesOutGoingTransferWord = wordDetectorRepo.getAll(WordDetectorType.EXPENSES_OUTGOING_TRANSFER_WORDS).map { it.word }
        val expensesPayBillsWord = wordDetectorRepo.getAll(WordDetectorType.EXPENSES_PAY_BILLS_WORDS).map { it.word }
        val expensesWithdrawalATMWord = wordDetectorRepo.getAll(WordDetectorType.WITHDRAWAL_ATM_WORDS).map { it.word }
        val incomesWord = wordDetectorRepo.getAll(WordDetectorType.INCOME_WORDS).map { it.word }

        return SmsUtils.getSmsType(
            sms = body,
            senderName=senderName,
            expensesPurchasesList = expensesPurchasesWord,
            expensesOutGoingTransferList = expensesOutGoingTransferWord,
            expensesPayBillsList = expensesPayBillsWord,
            expensesWithdrawalATMsList=expensesWithdrawalATMWord,
            incomesList = incomesWord
        )
    }
}