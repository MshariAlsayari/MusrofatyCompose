package com.msharialsayari.musrofaty.ui.screens.filter_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.CreateNewFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateFilterUseCase
import com.msharialsayari.musrofaty.utils.StringsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel@Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    private val createNewFilterUseCase: CreateNewFilterUseCase,
    private val updateFilterUseCase: UpdateFilterUseCase,
    private val deleteFilterUseCase: DeleteFilterUseCase,
    @ApplicationContext val context: Context
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState

    fun getFilter(filterId:Int){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getFilterUseCase(filterId)
            _uiState.update {
                it.copy(isLoading = false, words = result.words, title =result.title)
            }
        }

    }

    fun onFilterTitleChanged(value:String){
        _uiState.update {
            it.copy( title = value)
        }
    }

    fun validate(): Boolean {
        val title = uiState.value.title.trim()
        val word = uiState.value.words.trim()
        val titleValidationModel = ValidationModel()
        val wordValidationModel = ValidationModel()
        if (title.isEmpty()){
            titleValidationModel.isValid = false
            titleValidationModel.errorMsg = context.getString(R.string.validation_field_mandatory)
        } else if (StringsUtils.containSpecialCharacter(title)){
            titleValidationModel.isValid = false
            titleValidationModel.errorMsg = context.getString(R.string.validation_contain_special_character)
        }

        if (word.isEmpty()){
            wordValidationModel.isValid = false
            wordValidationModel.errorMsg = context.getString(R.string.validation_field_mandatory)
        }else if (StringsUtils.containSpecialCharacter(word)){
            wordValidationModel.isValid = false
            wordValidationModel.errorMsg = context.getString(R.string.validation_contain_special_character)
        }

        _uiState.update {
            it.copy( titleValidationModel = titleValidationModel, wordValidationModel = wordValidationModel)
        }

        return titleValidationModel.isValid && wordValidationModel.isValid
    }




    fun onSaveBtnClicked(){
        viewModelScope.launch {
            val model = FilterAdvancedModel(words = _uiState.value.words.trim(), title =_uiState.value.title.trim() , senderId = _uiState.value.senderId, id = _uiState.value.filterId )
            updateFilterUseCase.invoke(model)
        }
    }

    fun onCreateBtnClicked(){
        viewModelScope.launch {
            val model = FilterAdvancedModel(words = _uiState.value.words, title =_uiState.value.title , senderId = _uiState.value.senderId)
            createNewFilterUseCase.invoke(model)
        }

    }

    fun onDeleteBtnClicked(){
        viewModelScope.launch {
            deleteFilterUseCase.invoke(_uiState.value.filterId)
        }
    }

    fun onFilterWordChanged( value: String) {
        _uiState.update {
            it.copy( words = value)
        }

    }



    data class FilterUiState(
        var isLoading: Boolean = false,
        var senderId: Int = 0,
        var filterId: Int = 0,
        var title: String = "",
        var words: String = "",
        var isCreateNewFilter:Boolean = false,
        var titleValidationModel:ValidationModel = ValidationModel(),
        var wordValidationModel:ValidationModel = ValidationModel(),
        )
}