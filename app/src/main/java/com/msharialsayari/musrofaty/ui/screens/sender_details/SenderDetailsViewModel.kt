package com.msharialsayari.musrofaty.ui.screens.sender_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ActiveSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PinSenderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersDetailsViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase,
    private val pinSenderUseCase: PinSenderUseCase,
    private val activeSenderUseCase: ActiveSenderUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendersDetailsUiState())
    val uiState: StateFlow<SendersDetailsUiState> = _uiState

    fun getSenderModel(senderId:Int){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getSenderUseCase.invoke(senderId)
            _uiState.update {
                it.copy(isLoading = false,sender = result, isActive =result.isActive , isPin =result.isPined )
            }
        }

    }

    fun pinSender(pin:Boolean){
        viewModelScope.launch {
            pinSenderUseCase.invoke(senderId = uiState.value.sender?.id!!, pin = pin)
            _uiState.update { state ->
                state.copy(isLoading = false, isPin = pin)
            }
        }
    }

    fun activeSender(active:Boolean){
        viewModelScope.launch {
            activeSenderUseCase.invoke(senderId = uiState.value.sender?.id!!, active = active)
            _uiState.update { state ->
                state.copy(isLoading = false, isActive = active)
            }
        }
    }

     data class SendersDetailsUiState(
        val isLoading: Boolean = true,
        val sender: SenderModel? = null,
        val isActive: Boolean = false,
        val isPin: Boolean = false,
    ){

        fun pinSender(pin:Boolean):SenderModel?{
            sender?.isPined = pin
            return sender
        }

         fun activeSender(active:Boolean):SenderModel?{
             sender?.isActive = active
             return sender
         }
    }

}