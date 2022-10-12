package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteWordDetectorUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetWordDetectorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmsAnalysisViewModel @Inject constructor(
    private val getWordDetectorUseCase: GetWordDetectorUseCase,
    private val addWordDetectorUseCase: AddWordDetectorUseCase ,
    private val deleteWordDetectorUseCase: DeleteWordDetectorUseCase
):ViewModel() {

    private val _uiState = MutableStateFlow(SmsAnalysisUIState())
    val uiState: StateFlow<SmsAnalysisUIState> = _uiState

    init {
        getWordDetectors()
    }


    private fun getWordDetectors(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val currencyResult = getWordDetectorUseCase.invoke(WordDetectorType.CURRENCY_WORDS)
            val expensesResult = getWordDetectorUseCase.invoke(WordDetectorType.EXPENSES_WORDS)
            val incomesResult = getWordDetectorUseCase.invoke(WordDetectorType.INCOME_WORDS)

            _uiState.update {
                it.copy(isLoading = false, currencyList = currencyResult, expensesList = expensesResult, incomesList = incomesResult)
            }
        }

    }

    fun addWordDetector(value:String,type: WordDetectorType){
        viewModelScope.launch {
            val model = WordDetectorModel(word = value, type = type.name)
            addWordDetectorUseCase.invoke(model)
        }
    }

    fun deleteWordDetector(id:Int){
        viewModelScope.launch {
            deleteWordDetectorUseCase.invoke(id)
        }
    }


    data class SmsAnalysisUIState(
        var isLoading: Boolean = false,
        var currencyList: Flow<List<WordDetectorEntity>>? = null,
        var expensesList: Flow<List<WordDetectorEntity>>? = null,
        var incomesList: Flow<List<WordDetectorEntity>>? = null,
    )
}