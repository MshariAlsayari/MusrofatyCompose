package com.msharialsayari.musrofaty.pdf

import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.base.BaseViewModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFilterUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import com.msharialsayari.musrofaty.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


@HiltViewModel
class PdfCreatorViewModel @Inject constructor(
    private val getAllSmsUseCase: GetSmsModelListUseCase,
    private val getFilterUseCase: GetFilterUseCase,
    ) :
    BaseViewModel<PdfCreatorViewModel.PdfCreatorEvent>() {

    var endDate: Long = 0L
    var startDate: Long = 0L
    var filterWord: String = ""
    var filterTimeId: Int= 0
    var senderId: Int = 0
    var filterId: Int = -1
    var isFilter:Boolean= false
    private var filterAmount: FilterAmountModel? = null

    fun getAllBanksSms() {
        viewModelScope.launch {
            setEvent { PdfCreatorEvent.OnLoading(true) }
            val amountResult = getFilterUseCase.invoke(filterId)
            amountResult?.let {
                filterAmount = amountResult.amountFilter
            }
            val result = getAllSmsUseCase.invoke(
                senderId = senderId,
                filterOption = DateUtils.FilterOption.getFilterOptionOrDefault(filterTimeId),
                query = filterWord,
                filterAmountModel = filterAmount,
                isFilter = isFilter,
                startDate = startDate,
                endDate = endDate
            )
            setEvent { PdfCreatorEvent.OnGetSmsSuccess(result) }
            setEvent { PdfCreatorEvent.OnLoading(false) }
        }
    }
    sealed class PdfCreatorEvent {
        data class OnGetSmsSuccess(val list: List<SmsModel>) : PdfCreatorEvent()
        data class OnLoading(val showProgress: Boolean) : PdfCreatorEvent()
    }


    @Parcelize
    data class PdfBundle (
        var endDate: Long = 0L,
        var startDate: Long = 0L,
        var filterWord: String = "",
        var filterTimeId: Int= 0,
        var senderId: Int = 0,
        var filterId:Int= -1,
        var isFilter:Boolean= false
            ):Parcelable
}