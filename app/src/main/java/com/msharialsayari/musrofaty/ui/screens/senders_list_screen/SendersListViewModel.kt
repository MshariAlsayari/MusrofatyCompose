package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersListViewModel @Inject constructor(
    private val smsRepo: SmsRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendersUiState())
    val uiState  : StateFlow<SendersUiState> = _uiState

    init {
        getAllSenders()
    }

    fun getAllSenders(){
        viewModelScope.launch {
            val result = smsRepo.getAllNoCheckIsDeleted()
            _uiState.update { state ->
                state.copy(isLoading = false,senders =  result.groupBy { it.senderName!! })
            }
        }
    }


    data class SendersUiState(
        val isLoading: Boolean = true,
        val senders: Map<String, List<SmsModel>> = mapOf()
    )

}