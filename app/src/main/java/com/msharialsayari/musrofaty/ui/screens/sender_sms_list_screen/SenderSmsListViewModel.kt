package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderWithSmsUseCase
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenderSmsListViewModel @Inject constructor(
    private val getSenderWithSmsUseCase: GetSenderWithSmsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase


) : ViewModel() {

    private val _uiState = MutableStateFlow(SenderSmsListUiState())
    val uiState: StateFlow<SenderSmsListUiState> = _uiState

    fun getSenderWithAllSms(senderId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getSenderWithSmsUseCase.invoke(senderId)
            _uiState.update {
                it.copy(sender = result.sender, sms = result.sms, isLoading = false)
            }
        }

    }

    fun favoriteSms(id:String , favorite:Boolean){
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }


    data class SenderSmsListUiState(
        var isLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        val sender: SenderModel? = null,
        var sms: List<SmsModel> = emptyList()
    ) {

            fun wrapSendersToSenderComponentModelList(
                sms: List<SmsModel>,
                context: Context
            ): List<SmsComponentModel> {
                val list = mutableListOf<SmsComponentModel>()
                sms.forEach {
                    val model = SmsComponentModel(
                        id = it.id,
                        timestamp = it.timestamp,
                        isFavorite = it.isFavorite,
                        body = it.body,
                        currency = it.currency,
                        senderDisplayName = SenderModel.getDisplayName(context, it.senderModel),
                        senderCategory = ContentModel.getDisplayName(
                            context,
                            it.senderModel?.content
                        ),
                    )

                    list.add(model)
                }


                return list

            }



    }
}