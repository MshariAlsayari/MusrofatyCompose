package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenderSmsListViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase,


    ) : ViewModel() {

    private val _uiState = MutableStateFlow(SenderSmsListUiState())
    val uiState: StateFlow<SenderSmsListUiState> = _uiState

    fun getSenderModel(senderId: Int) {
        viewModelScope.launch {
            val result = getSenderUseCase.invoke(senderId)
            _uiState.update {
                it.copy(sender = result,)
            }
        }

    }


    data class SenderSmsListUiState(
        val sender: SenderModel? = null,
        var contents: List<SmsModel> = emptyList()
    )
}