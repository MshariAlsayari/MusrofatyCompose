package com.msharialsayari.musrofaty.ui.screens.sender_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersDetailsViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase
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
                it.copy(isLoading = false,sender = result)
            }
        }

    }

    data class SendersDetailsUiState(
        val isLoading: Boolean = true,
        val sender: SenderModel? = null
    )

}