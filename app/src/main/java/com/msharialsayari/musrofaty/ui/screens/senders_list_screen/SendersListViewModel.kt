package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFlowSendersUserCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PinSenderUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SenderComponentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersListViewModel @Inject constructor(
    private val getFlowSendersUserCase: GetFlowSendersUserCase,
    private val pinSenderUseCase: PinSenderUseCase,
    private val addSenderUseCase: AddSenderUseCase,
    private val deleteSenderUseCase: DeleteSenderUseCase,
    private val navigator: AppNavigator,
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
            val result = getFlowSendersUserCase.invoke()
            _uiState.update { state ->
                state.copy(isLoading = false, senders = result)
            }
        }
    }


    fun pinSender(senderId: Int) {
        viewModelScope.launch {
            pinSenderUseCase.invoke(senderId = senderId, pin = true)

        }

    }

    fun deleteSender(senderId: Int) {
        viewModelScope.launch {
            deleteSenderUseCase.invoke(id = senderId)
        }
    }

    fun addSender(senderName: String) {
        viewModelScope.launch {
            val model = SenderModel(
                senderName = senderName,
                displayNameAr = senderName,
                displayNameEn = senderName
            )
            addSenderUseCase.invoke(model)
        }
    }

    fun navigateToSenderDetails(senderId: Int){
        navigator.navigate(Screen.SenderDetailsScreen.route + "/${senderId}")
    }

    fun navigateToSenderSmsList(senderId: Int){
        navigator.navigate(Screen.SenderSmsListScreen.route + "/${senderId}")
    }
}