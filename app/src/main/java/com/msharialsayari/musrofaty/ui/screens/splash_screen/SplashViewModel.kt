package com.msharialsayari.musrofaty.ui.screens.splash_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val smsRepo: SmsRepo,
) : ViewModel() {


     private val _uiState = MutableStateFlow(SplashUiState())
     val uiState  : StateFlow<SplashUiState> = _uiState


     fun insertSms(){
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
        var isLoading:Boolean = true
    )

}