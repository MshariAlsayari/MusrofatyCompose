package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderWithRelationsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersListViewModel @Inject constructor(
    private val sendersRepo: SenderRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendersUiState())
    val uiState: StateFlow<SendersUiState> = _uiState

    init {
        getAllSenders()
    }

    fun getAllSenders() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true)
            }
            val result = sendersRepo.getAllSendersWithSms()
            _uiState.update { state ->
                state.copy(isLoading = false, senders = state.getSendersList(result))
            }
        }
    }


    data class SendersUiState(
        var isLoading: Boolean = false,
        var senders: Map<SenderModel, List<SmsModel>> = mapOf()

    ) {

        fun getSendersList(list: List<SenderWithRelationsModel>): Map<SenderModel, List<SmsModel>> {
            val map = mutableMapOf<SenderModel, List<SmsModel>>()
            list.groupBy { it.sender }.entries.map {
                map.putIfAbsent(it.key, it.value.flatMap { it.sms })
            }
             return map.toMap()
        }

    }


}