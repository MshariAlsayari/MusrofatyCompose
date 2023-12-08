package com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsListByQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreSmsListViewModel  @Inject constructor(
    private val getAllSmsUseCase: GetSmsListByQueryUseCase
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


}