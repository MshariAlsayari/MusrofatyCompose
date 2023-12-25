package com.msharialsayari.musrofaty.ui.screens.appearance_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Theme
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.simplemobiletools.calendar.domain.settings.GetAppAppearanceUseCase
import com.simplemobiletools.calendar.domain.settings.GetAppLanguageUseCase
import com.simplemobiletools.calendar.domain.settings.UpdateAppAppearanceUseCase
import com.simplemobiletools.calendar.domain.settings.UpdateAppLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val getAppAppearanceUseCase: GetAppAppearanceUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val updateAppAppearanceUseCase: UpdateAppAppearanceUseCase,
    private val updateAppLanguageUseCase: UpdateAppLanguageUseCase
):ViewModel() {


    private val _uiState = MutableStateFlow(AppearanceUIState())
    val uiState: StateFlow<AppearanceUIState> = _uiState

    companion object {
        val TAG = AppearanceViewModel::class.java.simpleName
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

            }
        }
    }

    fun updateAppAppearance(selectedTheme: Theme) {
        Log.d(TAG, "updateAppAppearance() theme:${selectedTheme.name} ")
        viewModelScope.launch {
            try {
                updateAppAppearanceUseCase(selectedTheme.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updateAppLanguage(selectedLanguage: Language) {
        Log.d(TAG, "updateAppLanguage() language:${selectedLanguage.name} ")
        viewModelScope.launch {
            try {
                updateAppLanguageUseCase(selectedLanguage.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun getLanguageOptions(context: Context): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        val options = context.resources.getStringArray(R.array.language_options)
        options.mapIndexed { index, value ->
            list.add(
                SelectedItemModel(
                id = index,
                value = value,
                isSelected = _uiState.value.appLanguage.id == index
            )
            )
        }

        return list

    }

    fun getThemeOptions(context: Context): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        val options = context.resources.getStringArray(R.array.theme_options)
        options.mapIndexed { index, value ->
            list.add(
                SelectedItemModel(
                id = index,
                value = value,
                isSelected = _uiState.value.appAppearance.id == index
            )
            )
        }

        return list

    }

}