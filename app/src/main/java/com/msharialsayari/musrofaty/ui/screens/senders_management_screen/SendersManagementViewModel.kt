package com.msharialsayari.musrofaty.ui.screens.senders_management_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddSenderUseCase

import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFlowSendersUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersManagementViewModel @Inject constructor(
    private val getSendersUseCase: GetFlowSendersUserCase,
    private val addSenderUseCase: AddSenderUseCase,
):ViewModel() {



    private val _uiState = MutableStateFlow(SendersManagementUiState())
    val uiState: StateFlow<SendersManagementUiState> = _uiState

    init {
        getSenders()

    }

    private fun getSenders(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val result = getSendersUseCase.invoke()


            _uiState.update {
                it.copy(isLoading = false, senders = result)
            }

        }
    }




    fun addSender(senderName:String){
        viewModelScope.launch {
        val model = SenderModel(senderName = senderName, displayNameAr = senderName, displayNameEn = senderName)
        addSenderUseCase.invoke(model)
        }
    }


    data class SendersManagementUiState(
        var isLoading:Boolean = false,
        var senders:Flow<List<SenderEntity>>? = null,
    )
}