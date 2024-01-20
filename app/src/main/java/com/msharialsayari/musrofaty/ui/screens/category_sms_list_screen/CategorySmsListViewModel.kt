package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.FavoriteSmsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetCategoryUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSendersUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.SoftDeleteSMsUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.utils.DateUtils
import com.patrykandpatrick.vico.core.extension.getFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySmsListViewModel @Inject constructor(
    private val getSmsModelListUseCase: GetSmsModelListUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val getSendersUseCase: GetSendersUseCase,
    private val softDeleteSMsUseCase: SoftDeleteSMsUseCase,
    private val favoriteSmsUseCase: FavoriteSmsUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val navigator: AppNavigator,
    @ApplicationContext val context: Context


) : ViewModel(){

    private val _uiState = MutableStateFlow(CategorySmsListUIState())
    val uiState: StateFlow<CategorySmsListUIState> = _uiState

    companion object{
        const val FILTER_OPTION_KEY = "filterOption"
        const val QUERY_KEY = "query"
        const val START_DATE_KEY = "startDate"
        const val END_DATE_KEY = "endDate"
        const val CATEGORY_ID_KEY = "categoryId"
        const val SMS_LIST_KEY = "smsList"
    }


    val categoryId: Int?
        get() {
            return savedStateHandle.get<Int>(CATEGORY_ID_KEY)
        }


    val smsList: List<SmsModel>
        get() {
            val jsonList = savedStateHandle.get<String>(SMS_LIST_KEY)
            val container = Gson().fromJson(jsonList, SmsContainer::class.java)
            return container.list
        }



    init {
        getCategory()
        getSms()
        getSenders()
    }

    private fun getSms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update {
                it.copy(isLoading = false, smsList = smsList)
            }
        }
    }

    private fun getSenders(){
        viewModelScope.launch {
            val result = getSendersUseCase.invoke()
            _uiState.update {
                it.copy(senders = result)
            }
        }

    }

    private fun getCategory() {
        viewModelScope.launch {
            if(categoryId != null){
                val category = getCategoryUseCase.invoke(categoryId!!)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        category = category ?: CategoryModel.getNoSelectedCategory(),
                    )

                }
            }



        }
    }

    fun getSenderById(senderId:Int): SenderModel?{
        return  _uiState.value.senders.find { it.id == senderId }
    }

    fun favoriteSms(id: String, favorite: Boolean) {
        viewModelScope.launch {
            favoriteSmsUseCase.invoke(id, favorite)
        }
    }

    fun softDelete(id: String, delete: Boolean) {
        viewModelScope.launch {
            softDeleteSMsUseCase.invoke(id, delete)
        }
    }

    fun navigateToSmsDetails(smsID: String) {
        navigator.navigate(Screen.SmsScreen.route + "/${smsID}")
    }

    fun navigateUp(){
        navigator.navigateUp()
    }

}