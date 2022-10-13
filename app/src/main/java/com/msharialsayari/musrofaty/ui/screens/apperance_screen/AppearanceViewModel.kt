package com.msharialsayari.musrofaty.ui.screens.apperance_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCurrentLanguageOptionUseCase
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val getCurrentLanguageOptionUseCase: GetCurrentLanguageOptionUseCase,
    @ApplicationContext val context:Context

):ViewModel() {


    private val _uiState = MutableStateFlow(AppearanceUIState())
    val uiState: StateFlow<AppearanceUIState> = _uiState

    init {
        getCurrentLanguage()
    }

    private fun getCurrentLanguage(){
        viewModelScope.launch {
            val index = getCurrentLanguageOptionUseCase.invoke(context)
            val options = context.resources.getStringArray(R.array.language_options)
            _uiState.update {
                it.copy(currentLanguageOption =     options[index])
            }

        }
    }

    data class AppearanceUIState(
        var isLoading:Boolean = false,
        var currentLanguageOption:String= ""
    )
}