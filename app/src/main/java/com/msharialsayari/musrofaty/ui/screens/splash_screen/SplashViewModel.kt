package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.WordsType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val smsRepo: SmsRepo,
    @ApplicationContext val context: Context
) : ViewModel() {


    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        initData()
    }


    private fun initData() {
        if (!SharedPreferenceManager.isDefaultSmsWordsListChanged(
                context,
                WordsType.INCOME_WORDS
            )
        ) {
            SharedPreferenceManager.saveArrayList(
                context,
                Constants.listIncomeWords,
                WordsType.INCOME_WORDS
            )
        }

        if (!SharedPreferenceManager.isDefaultSmsWordsListChanged(
                context,
                WordsType.EXPENSES_WORDS
            )
        ) {
            SharedPreferenceManager.saveArrayList(
                context,
                Constants.listExpenseWords,
                WordsType.EXPENSES_WORDS
            )
        }

        if (!SharedPreferenceManager.isDefaultSmsWordsListChanged(context, WordsType.BANKS_WORDS)) {
            SharedPreferenceManager.saveArrayList(
                context,
                Constants.listOfSenders,
                WordsType.BANKS_WORDS
            )
        }

        if (!SharedPreferenceManager.isDefaultSmsWordsListChanged(
                context,
                WordsType.CURRENCY_WORDS
            )
        ) {
            SharedPreferenceManager.saveArrayList(
                context,
                Constants.listCurrencyWords,
                WordsType.CURRENCY_WORDS
            )
        }

        loadAllData()
    }


    private fun loadAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            smsRepo.insert()
            _isLoading.value = false
        }
    }

    sealed class SplashScreenEvent {
        data class OnLoading(val showProgress: Boolean) : SplashScreenEvent()
    }
}