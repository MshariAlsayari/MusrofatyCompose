package com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsListByQueryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetStoreAndCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreSmsListViewModel  @Inject constructor(
    private val getAllSmsUseCase: GetSmsListByQueryUseCase,
    private val getSendersUseCase: GetSendersUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val navigator: AppNavigator
) : ViewModel(){

    private val _uiState = MutableStateFlow(StoreSmsListUiState())
    val uiState  : StateFlow<StoreSmsListUiState> = _uiState


     fun getAllSms(storeName:String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val smsResult = getAllSmsUseCase.invoke( query = storeName,)
            _uiState.update {
                it.copy(smsFlow = smsResult, isLoading = false)
            }
        }

    }

    fun getSenders(){
        viewModelScope.launch {
            val result = getSendersUseCase.invoke()
            _uiState.update {
                it.copy(senders = result)
            }
        }

    }

    fun getSenderById(senderId:Int):SenderModel?{
        return  _uiState.value.senders.find { it.id == senderId }
    }

    fun favoriteSms(id: String, favorite: Boolean) {
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }

    fun softDelete(id: String, delete: Boolean) {
        viewModelScope.launch {
            softDeleteSMsUseCase.invoke(id, delete)
        }
    }

    fun navigateToSmsDetails(smsID: String) {
        navigator.navigate(Screen.SmsScreen.route + "/${smsID}")
    }

    fun navigateUp(){
        navigator.navigateUp()
    }


}