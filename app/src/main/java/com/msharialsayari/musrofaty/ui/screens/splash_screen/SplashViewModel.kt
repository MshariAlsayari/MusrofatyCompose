package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.jobs.AutoCategoriesStoresJob
import com.msharialsayari.musrofaty.jobs.InitStoresJob
import com.msharialsayari.musrofaty.jobs.InsertSmsJob
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {


    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState


    fun initInsertStoresJob(context: Context){
        val initStoresWorker = OneTimeWorkRequestBuilder<InitStoresJob>().build()
        WorkManager.getInstance(context).enqueue(initStoresWorker)
    }
    fun initInsertSmsJob(context: Context){
        _uiState.update {
            it.copy(isLoading = true)
        }
        val initStoresWorker = OneTimeWorkRequestBuilder<InsertSmsJob>().build()
        WorkManager.getInstance(context).enqueue(initStoresWorker)
        _uiState.update {
            it.copy(isLoading = false)
        }
    }

    fun initCategoriesStoresJob(context: Context){
        val requestWorker = OneTimeWorkRequestBuilder<AutoCategoriesStoresJob>().build()
        WorkManager.getInstance(context).enqueue(requestWorker)
    }




}