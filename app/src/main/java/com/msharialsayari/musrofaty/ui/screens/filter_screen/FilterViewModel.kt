package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.lifecycle.ViewModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFiltersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel@Inject constructor(
    private val getFiltersUseCase: GetFiltersUseCase,
    ) : ViewModel() {
}