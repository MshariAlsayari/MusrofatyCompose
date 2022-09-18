package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ActiveSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PinSenderUseCase
import com.msharialsayari.musrofaty.ui_component.SenderComponentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context
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
            val result = activeSenderUseCase.invoke(senderName = senderName, active = false)
            _uiState.update { state ->
                state.copy(isLoading = false, senders = state.removeSender(senderName))
            }
        }

    }


    fun pinSender(senderName:String){
        viewModelScope.launch {
            val result = pinSenderUseCase.invoke(senderName = senderName, pin = true)
            _uiState.update { state ->
                state.copy(isLoading = false, senders = state.pinSender(senderName))
            }

        }

    }


    data class SendersUiState(
        var isLoading: Boolean = false,
        var senders:  Map<SenderModel, List<SmsModel>> = emptyMap(),
    ){

        companion object {
            fun wrapSendersToSenderComponentModelList(
                map: Map<SenderModel, List<SmsModel>>,
                context: Context
            ): List<SenderComponentModel> {
                val list = mutableListOf<SenderComponentModel>()
                map.map {
                    if (it.key.isPined) {
                        list.add(
                            0,
                            SenderComponentModel(
                                senderName = it.key.senderName,
                                displayName = SenderModel.getDisplayName(context, it.key),
                                senderType = ContentModel.getDisplayName(context, it.key.content),
                                smsTotal = it.value.size
                            )
                        )
                    } else {
                        list.add(
                            SenderComponentModel(
                                senderName = it.key.senderName,
                                displayName = SenderModel.getDisplayName(context, it.key),
                                senderType = ContentModel.getDisplayName(context, it.key.content),
                                smsTotal = it.value.size
                            )
                        )
                    }
                }
                return list

            }
        }

        fun removeSender(senderName:String): Map<SenderModel, List<SmsModel>> {
            return senders.filterKeys { it.senderName != senderName }
        }

        fun pinSender(senderName: String): Map<SenderModel, List<SmsModel>> {
            senders.map { it.key.isPined = it.key.senderName == senderName }
            return senders

        }

    }


}