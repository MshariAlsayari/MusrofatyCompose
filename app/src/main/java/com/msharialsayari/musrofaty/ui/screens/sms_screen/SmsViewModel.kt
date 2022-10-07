package com.msharialsayari.musrofaty.ui.screens.sms_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsUseCase
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsViewModel @Inject constructor(
    private val getSmsUseCase: GetSmsUseCase,
    private val getSenderUseCase: GetSenderUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
):ViewModel() {

    private val _uiState = MutableStateFlow(SmsUiState())
    val uiState: StateFlow<SmsUiState> = _uiState

    fun getData(id:String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val smsResult = getSmsUseCase.invoke(id)
            val senderResult = getSenderUseCase.invoke(smsResult.senderId)

            _uiState.update {
                it.copy(isLoading = false, sms = smsResult, sender = senderResult)
            }
        }

    }

    fun favoriteSms(id:String , favorite:Boolean){
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }


    fun wrapSendersToSenderComponentModel(
        sms: SmsModel,
        context: Context
    ): SmsComponentModel {

        return SmsComponentModel(
            id = sms.id,
            timestamp = sms.timestamp,
            isFavorite = sms.isFavorite,
            body = sms.body,
            senderDisplayName = SenderModel.getDisplayName(context, _uiState.value.sender),
            senderCategory = ContentModel.getDisplayName(context, _uiState.value.sender?.content)

        )

    }

    data class SmsUiState (
        var isLoading: Boolean = false,
        var sms :SmsModel? = null,
        var sender :SenderModel? = null
    )
}