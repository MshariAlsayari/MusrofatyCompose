package com.msharialsayari.musrofaty.ui.screens.splash_screen

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.base.BaseViewModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel@Inject constructor(private val smsRepo: SmsRepo): BaseViewModel<SplashViewModel.SplashScreenEvent>() {

    init {
        loadAllData()
    }


    private fun loadAllData(){
        viewModelScope.launch {
            setEvent { SplashScreenEvent.OnLoading(true) }
            smsRepo.insert()
            postEvent { SplashScreenEvent.OnLoadSmsSuccess }
            setEvent { SplashScreenEvent.OnLoading(false) }
        }

    }

    sealed class SplashScreenEvent {
        object OnLoadSmsSuccess: SplashScreenEvent()
        data class OnLoading(val showProgress:Boolean): SplashScreenEvent()
    }
}