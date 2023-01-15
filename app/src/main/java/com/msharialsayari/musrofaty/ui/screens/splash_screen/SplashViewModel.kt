package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.jobs.InsertSmsJob
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val smsRepo: SmsRepo,
) : ViewModel() {


    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState


    fun initInsertSmsJob(context: Context){
        val initStoresWorker = OneTimeWorkRequestBuilder<InsertSmsJob>().build()
        WorkManager.getInstance(context).enqueue(initStoresWorker)
    }


    data class SplashUiState(
        var isLoading: Boolean = false
    )

}