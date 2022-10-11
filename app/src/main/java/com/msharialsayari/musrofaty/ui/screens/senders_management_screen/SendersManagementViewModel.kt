package com.msharialsayari.musrofaty.ui.screens.senders_management_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersManagementViewModel @Inject constructor(
    private val getActiveSendersUseCase: GetActiveSendersUseCase,
    private val getUnActiveSendersUseCase: GetUnActiveSendersUseCase,
    private val activeSendersUseCase: ActiveSenderUseCase,
    private val addSenderUseCase: AddSenderUseCase,
    private val loadSmsUseCase: LoadSmsUseCase
):ViewModel() {



    private val _uiState = MutableStateFlow(SendersManagementUiState())
    val uiState: StateFlow<SendersManagementUiState> = _uiState

    init {
        getActiveSenders()
        getUnActiveSenders()
    }

    fun getActiveSenders(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val result = getActiveSendersUseCase.invoke()


            _uiState.update {
                it.copy(isLoading = false, activeSenders = result)
            }

        }
    }

    fun getUnActiveSenders(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val result = getUnActiveSendersUseCase.invoke()


            _uiState.update {
                it.copy(isLoading = false, unActiveSenders = result)
            }
        }
    }

    fun updateSenderVisibility(senderId:Int , visibility:Boolean){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            activeSendersUseCase.invoke(senderId,visibility)


            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun addSender(senderName:String){
        viewModelScope.launch {
        val model = SenderModel(senderName = senderName, displayNameAr = senderName, displayNameEn = senderName)
            addSenderUseCase.invoke(model)
        }
    }

    fun loadSms(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            loadSmsUseCase.invoke()

            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }


    data class SendersManagementUiState(
        var isLoading:Boolean = false,
        var activeSenders:Flow<List<SenderEntity>>? = null,
        var unActiveSenders:Flow<List<SenderEntity>>? = null
    )
}