package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddFilterWordUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel@Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    private val addFilterWordUseCase: AddFilterWordUseCase
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
                it.copy(isLoading = false, words = FilterAdvancedModel.getFilterWordsAsList(result.words), title =result.title)
            }
        }

    }

    fun onFilterTitleChanged(value:String){
        viewModelScope.launch {

        }
    }

    fun addFilterWord( value: String) {
        viewModelScope.launch {
            val newList = _uiState.value.words.toMutableList()
            newList.add(value)
            _uiState.update {
                it.copy(words = newList)
            }
        }
    }

    data class FilterUiState(
        var isLoading: Boolean = false,
        var title: String = "",
        var words: List<String> = emptyList(),

        )
}