package com.msharialsayari.musrofaty.ui.screens.appearance_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ChangeLanguageUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.ChangeThemeUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCurrentLanguageOptionUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCurrentThemeOptionUseCase
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.AppTheme
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
class AppearanceViewModel @Inject constructor(
    private val getCurrentLanguageOptionUseCase: GetCurrentLanguageOptionUseCase,
    private val getCurrentThemeOptionUseCase: GetCurrentThemeOptionUseCase,
    private val changeLanguageUseCase: ChangeLanguageUseCase,
    private val changeThemeUseCase: ChangeThemeUseCase,
    @ApplicationContext val context:Context

):ViewModel() {


    private val _uiState = MutableStateFlow(AppearanceUIState())
    val uiState: StateFlow<AppearanceUIState> = _uiState

    init {
        getCurrentLanguage()
        getCurrentTheme()
    }

    private fun getCurrentLanguage(){
        viewModelScope.launch {
            val index = getCurrentLanguageOptionUseCase.invoke(context)
            val options = context.resources.getStringArray(R.array.language_options)
            _uiState.update {
                it.copy(currentLanguageOption =    options[index], selectedCurrentLanguage = SelectedItemModel(id =index , value =  options[index], isSelected = true))
            }

        }
    }

    private fun getCurrentTheme(){
        viewModelScope.launch {
            val index = getCurrentThemeOptionUseCase.invoke(context)
            val options = context.resources.getStringArray(R.array.theme_options)
            _uiState.update {
                it.copy(currentThemeOption = options[index], selectedCurrentTheme = SelectedItemModel(id =index , value =  options[index], isSelected = true))
            }

        }
    }

    fun getLanguageOptions(selectedItem: SelectedItemModel? = null ): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        val newContext = SharedPreferenceManager.applyLanguage(context, SharedPreferenceManager.getLanguage(context))
        val options = newContext.resources.getStringArray(R.array.language_options)
        options.mapIndexed { index, value ->
            list.add(
                SelectedItemModel(
                id = index,
                value = value,
                isSelected = selectedItem?.id == index
            )
            )
        }

        return list

    }

    fun getThemeOptions(selectedItem: SelectedItemModel? = null ): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        val options = context.resources.getStringArray(R.array.theme_options)
        options.mapIndexed { index, value ->
            list.add(
                SelectedItemModel(
                id = index,
                value = value,
                isSelected = selectedItem?.id == index
            )
            )
        }

        return list

    }

    fun onLanguageSelected(selectedLanguage: SelectedItemModel) {
        _uiState.update {
            changeLanguageUseCase.invoke(context = context, locale = getLocale(selectedLanguage))
            val newContext = SharedPreferenceManager.applyLanguage(context,getLocale(selectedLanguage))
            val languageOptions = newContext.resources.getStringArray(R.array.language_options)
            val themeOptions = newContext.resources.getStringArray(R.array.theme_options)
            val language = languageOptions[selectedLanguage.id]
            val theme = themeOptions[_uiState.value.selectedCurrentTheme?.id?:0]
            it.copy(currentLanguageOption = language , selectedCurrentLanguage = selectedLanguage, currentThemeOption = theme)
        }
    }

    fun onThemeSelected(selectedTheme: SelectedItemModel) {
        _uiState.update {
            changeThemeUseCase.invoke(context = context, appTheme = AppTheme.getThemById(selectedTheme.id))
            val options = context.resources.getStringArray(R.array.theme_options)
            val theme = options[selectedTheme.id]
            it.copy(currentThemeOption = theme , selectedCurrentTheme = selectedTheme)
        }
    }

    private fun getLocale(selectedLanguage: SelectedItemModel):Locale{
        return when(selectedLanguage.id){
            0-> Locale("ar")
            1-> Locale(Locale.ENGLISH.language)
            else -> Locale("ar")


        }

    }
}