package com.msharialsayari.musrofaty.ui.screens.filter_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.AmountOperators
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.LogicOperators
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteFilterAmountUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.DeleteFilterWordUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.InsertFilterAmountUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.InsertFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.InsertFilterWordUseCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType
import com.msharialsayari.musrofaty.utils.StringsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel@Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    private val insertFilterUseCase: InsertFilterUseCase,
    private val insertFilterWordUseCase: InsertFilterWordUseCase,
    private val insertFilterAmountUseCase: InsertFilterAmountUseCase,
    private val deleteFilterUseCase: DeleteFilterUseCase,
    private val deleteFilterWordUseCase: DeleteFilterWordUseCase,
    private val deleteFilterAmountUseCase: DeleteFilterAmountUseCase,
    private val navigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState

    companion object{
        const val SENDER_ID_KEY = "senderIdKey"
        const val FILTER_ID_KEY = "filterIdKey"
        const val CREATE_FILTER_KEY = "createFilterKey"
    }


    private val senderId: Int
        get() {
            return savedStateHandle.get<Int>(SENDER_ID_KEY) ?: -1
        }

    private val filterId: Int
        get() {
            return savedStateHandle.get<Int>(FILTER_ID_KEY) ?: -1
        }

    private val isCreateFilter: Boolean
        get() {
            return savedStateHandle.get<Boolean>(CREATE_FILTER_KEY) ?: false
        }

    init {
        _uiState.value.filterId = filterId
        _uiState.value.senderId = senderId
        _uiState.value.filterModel.id = filterId
        _uiState.value.filterModel.senderId = senderId
        _uiState.value.isCreateNewFilter = isCreateFilter
        getFilter()
    }


    private fun getFilter(){
        viewModelScope.launch {
            val result = getFilterUseCase(filterId)
            result?.let {model->
                _uiState.update {
                    it.copy(
                        filterModel = model.filter,
                        filterWords = model.words,
                        filterAmountModel = model.amountFilter
                    )
                }
            }

        }

    }


    fun validate(): Boolean {
        val title = uiState.value.filterModel.title.trim()
        val titleValidationModel = ValidationModel()
        if (title.isEmpty()){
            titleValidationModel.isValid = false
            titleValidationModel.errorMsg = context.getString(R.string.validation_field_mandatory)
        } else if (StringsUtils.containSpecialCharacter(title)){
            titleValidationModel.isValid = false
            titleValidationModel.errorMsg = context.getString(R.string.validation_contain_special_character)
        }

        _uiState.update {
            it.copy( titleValidationModel = titleValidationModel)
        }

        return titleValidationModel.isValid
    }

    fun onDeleteBtnClicked(){
        viewModelScope.launch {
            deleteFilterUseCase.invoke(_uiState.value.filterId)
        }
    }


    fun addFilter(
        item: FilterWordModel?,
        newWord: String) {
        viewModelScope.launch {
            val validateWord = newWord.replace(",", "")
            val filterId = _uiState.value.filterId
            val words = _uiState.value.filterWords.map { it.word }
            if (validateWord.isNotEmpty() && !words.contains(newWord)) {
                val newList: List<FilterWordModel>
                //create a new word
                if (item == null) {
                    val model = FilterWordModel(
                        word = newWord,
                        filterId = filterId,
                        logicOperator = LogicOperators.AND
                    )

                    newList = _uiState.value.filterWords.toMutableList().apply {
                        add(model)
                    }

                } else {   //update an existed word
                    val index = _uiState.value.filterWords.indexOfFirst { it.word == item.word }
                    val newItem = _uiState.value.filterWords[index].copy(word = newWord)
                    newList = _uiState.value.filterWords.toMutableList().apply {
                        removeAt(index)
                        add(index, newItem)
                    }
                }

                _uiState.update {
                    it.copy(filterWords = newList)
                }

            }
        }

    }


    fun addAmountFilter(
        item: FilterAmountModel?,
        newAmount: String) {
        viewModelScope.launch {
            val filterId = _uiState.value.filterId
            val filterAmountModel:FilterAmountModel
            if (newAmount.isNotEmpty()) {
                //create a new word
                filterAmountModel = if (item == null || _uiState.value.filterAmountModel == null ) {
                    FilterAmountModel(
                        amount = newAmount,
                        filterId = filterId,
                        amountOperator = AmountOperators.EQUAL_OR_MORE
                    )
                } else {   //update an existed word
                    _uiState.value.filterAmountModel?.copy(amount = newAmount)!!
                }

                _uiState.update {
                    it.copy(filterAmountModel = filterAmountModel)
                }

            }
        }

    }

    fun deleteFilter(word: FilterWordModel) {
         viewModelScope.launch {
             val newList = _uiState.value.filterWords.toMutableList().apply {
                 removeIf { it.word ==  word.word}
             }

             _uiState.update {
                 it.copy( filterWords = newList)
             }

             deleteFilterWordUseCase.invoke(word.wordId)
         }
    }

    fun deleteFilterAmount() {
        viewModelScope.launch {
            val model = _uiState.value.filterAmountModel
            model?.let {
                deleteFilterAmountUseCase.invoke(it.amountId)
            }
            _uiState.update {
                it.copy(filterAmountModel = null)
            }

        }
    }

    fun navigateUp(){
        navigator.navigateUp()
    }


    fun changeLogicOperator(item: FilterWordModel, logicOperators: LogicOperators) {
         val index = _uiState.value.filterWords.indexOfFirst { it.word == item.word }
         val newItem = _uiState.value.filterWords[index].copy(logicOperator = logicOperators)
        val newList =  _uiState.value.filterWords.toMutableList().apply {
            removeAt(index)
            add(index, newItem)
        }

        _uiState.update {
            it.copy(filterWords = newList)
        }
    }

    fun changeAmountLogicOperator() {
        val item = _uiState.value.filterAmountModel
        item?.let {

            val newLogicOperator = when(it.amountOperator){
                AmountOperators.EQUAL_OR_MORE -> AmountOperators.EQUAL_OR_LESS
                AmountOperators.EQUAL_OR_LESS ->  AmountOperators.EQUAL
                AmountOperators.EQUAL ->  AmountOperators.EQUAL_OR_MORE
            }

            val newItem = it.copy(
                amountOperator = newLogicOperator
            )

            _uiState.update {
                it.copy(filterAmountModel = newItem)
            }
        }
    }

    fun openBottomSheet(type:FilterBottomSheetType?){
        _uiState.update {
            it.copy(bottomSheetType = type)
        }

    }

    fun onFilterTitleChanged(title: String) {
        val newFilter = _uiState.value.filterModel.copy(
            title = title
        )

        _uiState.update {
            it.copy(filterModel = newFilter)
        }
    }

    fun onCreateBtnClicked() {
        viewModelScope.launch {
            val filter =  _uiState.value.filterModel
            if(isCreateFilter){
                filter.id = 0
            }
            val filterId = insertFilterUseCase.invoke(filter).first()

            val words =  _uiState.value.filterWords.map {
                it.filterId = filterId.toInt()
                it
            }

            words.map { word->
                insertFilterWordUseCase.invoke(word)
            }

            _uiState.value.filterAmountModel?.let {
                insertFilterAmountUseCase.invoke(it)
            }
        }
    }


    fun isLastItem(item: FilterWordModel): Boolean {
        val index = _uiState.value.filterWords.indexOfFirst { it.word == item.word }
        return _uiState.value.filterWords.lastIndex == index
    }


}