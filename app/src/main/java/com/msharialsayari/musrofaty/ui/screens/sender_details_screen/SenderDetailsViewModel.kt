package com.msharialsayari.musrofaty.ui.screens.sender_details_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.*
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendersDetailsViewModel @Inject constructor(
    private val getSenderUseCase: GetSenderUseCase,
    private val pinSenderUseCase: PinSenderUseCase,
    private val updateSenderDisplayNameUseCase: UpdateSenderDisplayNameUseCase,
    private val getContentByKeyUseCase: GetContentByKeyUseCase,
    private val updateSenderCategoryUseCase: UpdateSenderCategoryUseCase,
    private val deleteSenderUseCase: DeleteSenderUseCase,
    private val addContentUseCase: AddContentUseCase,
    private val navigator: AppNavigator

) : ViewModel() {

    private val _uiState = MutableStateFlow(SendersDetailsUiState())
    val uiState: StateFlow<SendersDetailsUiState> = _uiState

    init {
        getSendersContent()
    }


    private fun getSendersContent() {
        viewModelScope.launch {
            val result = getContentByKeyUseCase.invoke(ContentKey.SENDERS)
            _uiState.update {
                it.copy(
                    contents = result,
                )
            }
        }
    }

    fun getSenderModel(senderId: Int) {
        viewModelScope.launch {
            val result = getSenderUseCase.invoke(senderId)
            if (result != null)
                _uiState.update {
                    it.copy(
                        sender = result,
                        isPin = result.isPined
                    )
                }
        }

    }

    fun getSenderContentDisplayName(context: Context): String {
        return if (ContentModel.getDisplayName(
                context = context,
                _uiState.value.sender?.content
            ).isNotEmpty()
        ) ContentModel.getDisplayName(
            context = context,
            _uiState.value.sender?.content
        ) else context.getString(androidx.compose.ui.R.string.not_selected)
    }

    fun pinSender(pin: Boolean) {
        viewModelScope.launch {
            pinSenderUseCase.invoke(senderId = uiState.value.sender?.id!!, pin = pin)
            _uiState.update { state ->
                state.copy(isPin = pin)
            }
        }
    }


    fun deleteSender() {
        viewModelScope.launch {
            deleteSenderUseCase.invoke(id = uiState.value.sender?.id!!)
        }
    }

    fun addContent(model: ContentModel) {
        viewModelScope.launch {
            addContentUseCase.invoke(model)
        }
    }

    fun changeDisplayName(name: String, isArabic: Boolean) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                updateSenderDisplayNameUseCase.invoke(
                    senderId = uiState.value.sender?.id!!,
                    isArabicName = isArabic,
                    name = name
                )
                _uiState.update { state ->
                    if (isArabic)
                        state.copy(sender = state.changeArabicDisplayName(name))
                    else
                        state.copy(sender = state.changeEnglishDisplayName(name))
                }
            }
        }
    }

    fun updateSenderCategory(category: SelectedItemModel) {
        viewModelScope.launch {
            _uiState.value.sender?.let { sender ->
                updateSenderCategoryUseCase.invoke(
                    senderId = sender.id,
                    contentCategoryId = category.id
                )

                _uiState.update { state ->
                    state.copy(sender = state.updateSenderCategory(category.id))
                }
            }
        }

    }

    fun navigateUp(){
        navigator.navigateUp()
    }

    fun navigateToContent(id:Int){
        navigator.navigate(Screen.ContentScreen.route + "/${id}")

    }


}