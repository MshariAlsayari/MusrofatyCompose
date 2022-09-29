package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetPagesSmsList
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenderSmsListViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getPagesSmsList: GetPagesSmsList


) : ViewModel() {

    private val _uiState = MutableStateFlow(SenderSmsListUiState())
    val uiState: StateFlow<SenderSmsListUiState> = _uiState

    fun getSenderWithAllSms(senderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val senderResult = getSenderUseCase.invoke(senderId)
            val smsResult    = getPagesSmsList.invoke(senderId).cachedIn(viewModelScope)
            _uiState.update {
                it.copy(sender = senderResult, smsFlow = smsResult, isLoading = false, )
            }
        }

    }


    fun favoriteSms(id:String , favorite:Boolean){
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }


    fun wrapSendersToSenderComponentModelList(
        sms: List<SmsEntity>,
        context: Context
    ): List<SmsComponentModel> {
        val list = mutableListOf<SmsComponentModel>()
        sms.forEach {
            list.add(wrapSendersToSenderComponentModel(it,context))
        }
        return list

    }

    fun wrapSendersToSenderComponentModel(
        sms: SmsEntity,
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


    data class SenderSmsListUiState(
        var isLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        val sender: SenderModel? = null,
        var smsFlow: Flow<PagingData<SmsEntity>>? =null
    )
}