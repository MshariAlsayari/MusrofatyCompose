package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.toWordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ObserveWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.UpdateWordDetectorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmsAnalysisViewModel @Inject constructor(
    private val getWordDetectorUseCase: ObserveWordDetectorUseCase,
    private val addWordDetectorUseCase: AddWordDetectorUseCase,
    private val updateWordDetectorUseCase: UpdateWordDetectorUseCase,
    private val deleteWordDetectorUseCase: DeleteWordDetectorUseCase
    ):ViewModel() {

    private val _uiState = MutableStateFlow(SmsAnalysisUIState())
    val uiState: StateFlow<SmsAnalysisUIState> = _uiState

    init {
        observingData()
    }

    private fun observingData() {
        viewModelScope.launch {
            val result = getWordDetectorUseCase.invoke()
            _uiState.update {
                it.copy(list = result)
            }
        }
    }
    fun updateSelectedTab(index:Int){
        _uiState.update {
            it.copy(
                selectedTab = index
            )
        }
    }

    fun updateScreenType(isSmsAnalysisScreen: Boolean){
        _uiState.update {
            it.copy(
                isSmsAnalyticsScreen = isSmsAnalysisScreen
            )
        }
    }

    private fun addWordDetector(value:String,type: WordDetectorType){
        viewModelScope.launch {
            val model = WordDetectorModel(word = value, type = type.name)
            addWordDetectorUseCase.invoke(model)
        }
    }

    private fun updateWordDetector(value: String, selectedItem: WordDetectorEntity) {
        val newItem = selectedItem.toWordDetectorModel()
        newItem.word = value
        viewModelScope.launch {
            updateWordDetectorUseCase.invoke(newItem)
        }
    }

    fun deleteWordDetector(id:Int){
        viewModelScope.launch {
            deleteWordDetectorUseCase.invoke(id)
        }
    }

    fun getWordDetectorByIndex(index: Int): WordDetectorType {
        val isSmsAnalyticsScreen = _uiState.value.isSmsAnalyticsScreen
        return if(isSmsAnalyticsScreen){
            WordDetectorType.getTypeByIndexForAnalyticsScreen(index)
        }else{
            WordDetectorType.getTypeByIndexForSmsTypesScreen(index)
        }

    }

    fun getTabList(isSmsAnalysisScreen: Boolean): List<Int> {
        return if(isSmsAnalysisScreen){
            WordDetectorType.getAnalyticsScreenList().sortedBy { it.id }.map { it.value }
        }else{
            WordDetectorType.getSmsTypesScreenList().sortedBy { it.id }.map { it.value }
        }
    }

    private fun getWordDetectorType(): WordDetectorType {
        val tabIndex = _uiState.value.selectedTab
        val isSmsAnalyticsScreen = _uiState.value.isSmsAnalyticsScreen
        return if(isSmsAnalyticsScreen){
            WordDetectorType.getTypeByIndexForAnalyticsScreen(tabIndex)
        }else{
            WordDetectorType.getTypeByIndexForSmsTypesScreen(tabIndex)
        }
    }



    fun onActionClicked(value: String,selectedItem:WordDetectorEntity?){
        val type =  getWordDetectorType()
        val validatedWord = value.trim()
        if(validatedWord.isNotEmpty()){
            if(selectedItem != null){
                updateWordDetector(value, selectedItem)
            }else{
                addWordDetector(value, type)
            }
        }

    }


}