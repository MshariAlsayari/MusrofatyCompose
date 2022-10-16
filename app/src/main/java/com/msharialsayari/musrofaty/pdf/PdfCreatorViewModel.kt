package com.msharialsayari.musrofaty.pdf

import com.msharialsayari.musrofaty.base.BaseViewModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllSmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PdfCreatorViewModel @Inject constructor(private val getAllSmsUseCase: GetAllSmsUseCase) :
    BaseViewModel<PdfCreatorViewModel.PdfCreatorEvent>() {

    var bankName:String = ""
    var generateAllSms:Boolean = false

    fun getAllBanksSms() {
//        viewModelScope.launch {
//            setEvent { PdfCreatorEvent.OnLoading(true) }
//            val result = getAllSmsUseCase.invoke(bankName = bankName)
//            setEvent { PdfCreatorEvent.OnGetSmsSuccess(result) }
//            setEvent { PdfCreatorEvent.OnLoading(false) }
//        }
    }
    sealed class PdfCreatorEvent {
        data class OnGetSmsSuccess(val list: List<SmsModel>) : PdfCreatorEvent()
        data class OnLoading(val showProgress: Boolean) : PdfCreatorEvent()
    }
}