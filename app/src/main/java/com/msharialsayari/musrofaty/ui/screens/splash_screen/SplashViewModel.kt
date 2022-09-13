package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.base.BaseViewModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.WordsType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val smsRepo: SmsRepo,
    @ApplicationContext val context: Context
) : BaseViewModel<SplashViewModel.SplashScreenEvent>() {

    val isLoading = mutableStateOf(true)

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
            isLoading.value =true
            smsRepo.insert()
            isLoading.value =false
        }
    }

    sealed class SplashScreenEvent {
        data class OnLoading(val showProgress: Boolean) : SplashScreenEvent()
    }
}