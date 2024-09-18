package com.msharialsayari.musrofaty.ui.screens.sms_types_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetWordDetectorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmsTypesViewModel @Inject constructor(
    private val getWordDetectorUseCase: GetWordDetectorUseCase,
    private val addWordDetectorUseCase: AddWordDetectorUseCase,
    private val deleteWordDetectorUseCase: DeleteWordDetectorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SmsTypesUiState())
    val uiState: StateFlow<SmsTypesUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val initialPage: Flow<List<WordDetectorEntity>> =
        _uiState.flatMapLatest { uiState ->
            val type = getWordDetectorByIndex(uiState.selectedTab)
             getWordDetectorUseCase(type)
        }


    fun updateSelectedTab(index:Int){
        _uiState.update {
            it.copy(
                selectedTab = index
            )
        }
    }


    fun addWordDetector(value:String){
        val type = WordDetectorType.getTypeByIndexForSmsTypesScreen(_uiState.value.selectedTab)
        viewModelScope.launch {
            val model = WordDetectorModel(word = value, type = type.name)
            addWordDetectorUseCase.invoke(model)
        }
    }

    fun deleteWordDetector(id: Int){
        viewModelScope.launch {
            deleteWordDetectorUseCase.invoke(id)
        }
    }

    fun getWordDetectorByIndex(index: Int): WordDetectorType {
        return WordDetectorType.getTypeByIndexForSmsTypesScreen(index)
    }
}