package com.msharialsayari.musrofaty

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Theme
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.simplemobiletools.calendar.domain.settings.GetAppAppearanceUseCase
import com.simplemobiletools.calendar.domain.settings.GetAppLanguageUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
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
    private val getAppAppearanceUseCase: GetAppAppearanceUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    @ApplicationContext private val context:Context
):ViewModel() {


    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    companion object {
        val TAG = MainViewModel::class.java.simpleName
    }

    init {
        observeAppTheme()
        observeAppLanguage()
    }


    private fun observeAppTheme() {
        viewModelScope.launch {
            getAppAppearanceUseCase().collect { id ->
                val theme = Theme.getThemeById(id)
                Log.d(TAG, "observeAppTheme() theme:${theme.name} ")
                _uiState.update {
                    it.copy(
                        appAppearance = theme
                    )
                }

            }

        }
    }

    private fun observeAppLanguage() {
        viewModelScope.launch {
            getAppLanguageUseCase().collect { id ->
                val language = Language.getLanguageById(id)
                Log.d(TAG, "observeAppLanguage() language:${language.name} ")
                _uiState.update {
                    it.copy(
                        appLanguage = language
                    )
                }
                setAppLanguage()

            }
        }
    }


    private fun setAppLanguage() {
        val language = _uiState.value.appLanguage
        val locale = if (language == Language.DEFAULT) {
            Locale(Locale.getDefault().language)
        } else
            Locale(language.shortcut)


        Locale.setDefault(locale)
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(locale.language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }



    data class MainUiState(
        var currentLocale:Locale=Locale(Constants.arabic_ar),
        var currentTheme: AppTheme = AppTheme.System,

        val appAppearance: Theme = Theme.DEFAULT,
        val appLanguage: Language = Language.DEFAULT
    )
}