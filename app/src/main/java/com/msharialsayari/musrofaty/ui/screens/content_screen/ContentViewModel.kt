package com.msharialsayari.musrofaty.ui.screens.content_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteContentUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetContentUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateContentUseCase
import com.msharialsayari.musrofaty.utils.notEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContentViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    private val updateContentUseCase: UpdateContentUseCase,
    private val deleteContentUseCase: DeleteContentUseCase,
    @ApplicationContext val context: Context

) : ViewModel() {

    private val _uiState = MutableStateFlow(ContentUIState())
    val uiState: StateFlow<ContentUIState> = _uiState

    fun getContent(id: Int) {
        viewModelScope.launch {
            val result = getContentUseCase.invoke(id)
            _uiState.update {
                it.copy(content = result, valueAr =result?.valueAr?:"" , valueEn = result?.valueEn?:"")
            }

        }
    }

    fun deleteContent(){
        viewModelScope.launch {
            _uiState.value.content?.id?.let { deleteContentUseCase.invoke(it) }
        }
    }

    fun updateContent(){
        viewModelScope.launch {
            _uiState.value.content?.valueAr =  _uiState.value.valueAr
            _uiState.value.content?.valueEn =  _uiState.value.valueEn
            _uiState.value.content?.let { updateContentUseCase.invoke(it) }
        }
    }

    fun onArabicValueChanged(value:String){
        _uiState.update {
            it.copy(valueAr = value)
        }

    }

    fun onEnglishValueChanged(value:String){
        _uiState.update {
            it.copy(valueEn = value)
        }

    }

    fun validate(): Boolean {
        val valueAr = uiState.value.valueAr.trim()
        val valueEn = uiState.value.valueEn.trim()
        val valueArValidationModel = ValidationModel()
        val valueEnValidationModel = ValidationModel()
        if (!valueAr.notEmpty()){
            valueArValidationModel.isValid = false
            valueArValidationModel.errorMsg = context.getString(R.string.validation_field_mandatory)
        }

        if (!valueEn.notEmpty()){
            valueEnValidationModel.isValid = false
            valueEnValidationModel.errorMsg = context.getString(R.string.validation_field_mandatory)
        }

        _uiState.update {
            it.copy( valueArValidationModel = valueArValidationModel, valueEnValidationModel = valueEnValidationModel)
        }

        return valueArValidationModel.isValid && valueEnValidationModel.isValid
    }


}