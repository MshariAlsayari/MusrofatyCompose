package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ActiveSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PinSenderUseCase
import com.msharialsayari.musrofaty.ui_component.SenderComponentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersListViewModel @Inject constructor(
    private val getSendersUseCase: GetSendersUseCase,
    private val activeSenderUseCase: ActiveSenderUseCase,
    private val pinSenderUseCase: PinSenderUseCase,
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
            val result = getSendersUseCase.invoke()
            _uiState.update { state ->
                state.copy(isLoading = false, senders = result)
            }
        }
    }

    fun disableSender(senderName:String){
        viewModelScope.launch {
             activeSenderUseCase.invoke(senderName = senderName, active = false)
            getAllSenders()
        }

    }


    fun pinSender(senderName:String){
        viewModelScope.launch {
            pinSenderUseCase.invoke(senderName = senderName, pin = true)
            getAllSenders()

        }

    }


    data class SendersUiState(
        var isLoading: Boolean = false,
        var senders: List<SenderModel> = emptyList(),
    ){

        companion object {
            fun wrapSendersToSenderComponentModelList(
                senders: List<SenderModel>,
                context: Context
            ): List<SenderComponentModel> {
                val list = mutableListOf<SenderComponentModel>()
                senders.map {
                    if (it.isPined) {
                        list.add(
                            0,
                            SenderComponentModel(
                                senderName = it.senderName,
                                displayName = SenderModel.getDisplayName(context, it),
                                senderType = ContentModel.getDisplayName(context, it.content),
                            )
                        )
                    } else {
                        list.add(
                            SenderComponentModel(
                                senderName = it.senderName,
                                displayName = SenderModel.getDisplayName(context, it),
                                senderType = ContentModel.getDisplayName(context, it.content),
                            )
                        )
                    }
                }
                return list

            }
        }


    }


}