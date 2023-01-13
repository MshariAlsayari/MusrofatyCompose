package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.jobs.InitAppJob
import com.msharialsayari.musrofaty.jobs.InitCategoriesJob
import com.msharialsayari.musrofaty.jobs.InitStoresJob
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val smsRepo: SmsRepo,
    @ApplicationContext val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        initData()
    }

    private fun initData()
    {
        initCategoriesJob()
        initStoresJob()

        if (SharedPreferenceManager.isFirstLunch(context)) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            initAppJob()

            SharedPreferenceManager.setFirstLunch(context)
            _uiState.update {
                it.copy(isLoading = false)
            }
        } else {
            insertSms()
        }

    }


    private fun initAppJob(){
        val initAppWorker = OneTimeWorkRequestBuilder<InitAppJob>().build()
        WorkManager.getInstance(context).enqueue(initAppWorker)
    }

    private fun initCategoriesJob(){
        val initCategoriesWorker = OneTimeWorkRequestBuilder<InitCategoriesJob>().build()
        WorkManager.getInstance(context).enqueue(initCategoriesWorker)
    }

    private fun initStoresJob(){
        val initStoresWorker = OneTimeWorkRequestBuilder<InitStoresJob>().build()
        WorkManager.getInstance(context).enqueue(initStoresWorker)
    }



    private fun insertSms() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            smsRepo.insert()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }

    }


    data class SplashUiState(
        var isLoading: Boolean = false
    )

}