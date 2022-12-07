package com.msharialsayari.musrofaty

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context:Context
):ViewModel() {


    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        updateLanguage()
        updateTheme()
    }

    fun updateLanguage(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(currentLocale = SharedPreferenceManager.getLanguage(context))
            }
            setAppLanguage(_uiState.value.currentLocale)
        }

    }

    fun updateTheme(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(currentTheme = AppTheme.getThemById(SharedPreferenceManager.getTheme(context)))
            }
        }

    }

    private fun setAppLanguage(locale: Locale) {
        Locale.setDefault(locale)
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(locale.language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }



    data class MainUiState(
        var currentLocale:Locale=Locale(Constants.arabic_ar),
        var currentTheme: AppTheme = AppTheme.System
    )
}