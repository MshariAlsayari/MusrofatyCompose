package com.msharialsayari.musrofaty.ui.screens.sender_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ActiveSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PinSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateSenderDisplayNameUseCase
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
    private val updateSenderDisplayNameUseCase: UpdateSenderDisplayNameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendersDetailsUiState())
    val uiState: StateFlow<SendersDetailsUiState> = _uiState

    fun getSenderModel(senderId: Int) {
        viewModelScope.launch {
            val result = getSenderUseCase.invoke(senderId)
            _uiState.update {
                it.copy(
                    sender = result,
                    isActive = result.isActive,
                    isPin = result.isPined
                )
            }
        }

    }

    fun pinSender(pin: Boolean) {
        viewModelScope.launch {
            pinSenderUseCase.invoke(senderId = uiState.value.sender?.id!!, pin = pin)
            _uiState.update { state ->
                state.copy(isPin = pin)
            }
        }
    }

    fun activeSender(active: Boolean) {
        viewModelScope.launch {
            activeSenderUseCase.invoke(senderId = uiState.value.sender?.id!!, active = active)
            _uiState.update { state ->
                state.copy( isActive = active)
            }
        }
    }

    fun changeDisplayName(name: String, isArabic: Boolean) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                updateSenderDisplayNameUseCase.invoke(
                    senderId = uiState.value.sender?.id!!,
                    isArabicName = isArabic,
                    name = name
                )
                _uiState.update { state ->
                    if (isArabic)
                        state.copy(sender = state.changeArabicDisplayName(name))
                    else
                        state.copy(sender = state.changeEnglishDisplayName(name))
                }
            }
        }
    }

    data class SendersDetailsUiState(
        val sender: SenderModel? = null,
        val isActive: Boolean = false,
        val isPin: Boolean = false,
    ) {

        fun pinSender(pin: Boolean): SenderModel? {
            val model = sender
            model?.isPined = pin
            return sender
        }

        fun activeSender(active: Boolean): SenderModel? {
            val model = sender
            model?.isActive = active
            return sender
        }

        fun changeArabicDisplayName(name: String): SenderModel? {
            val model = sender
            model?.displayNameAr = name
            return model

        }

        fun changeEnglishDisplayName(name: String): SenderModel? {
            val model = sender
            model?.displayNameEn = name
            return model

        }

    }

}