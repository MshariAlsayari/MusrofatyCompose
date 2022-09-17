package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.ContentRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
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
    private val wordDetectorRepo: WordDetectorRepo,
    private val contentRepo: ContentRepo,
    private val senderRepo: SenderRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {


     private val _uiState = MutableStateFlow(SplashUiState())
     val uiState  : StateFlow<SplashUiState> = _uiState

    init {
        initData()
    }

    private fun insertContent(){
        viewModelScope.launch {
            val defaultContent = ContentModel.getDefaultContent()
            contentRepo.insert(*defaultContent.toTypedArray())
        }

    }

    private fun insertSms(){
        viewModelScope.launch {
            smsRepo.insert()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }

    }

    private fun insertWordsDetector(){
        viewModelScope.launch {
            wordDetectorRepo.insertDefault()
        }

    }

    private fun insertDefaultSenders(){
        viewModelScope.launch {
            val senders = SenderModel.getDefaultSendersModel()
            senderRepo.insert(*senders.toTypedArray())
        }

    }


    private fun initData() {
        val isFirst = SharedPreferenceManager.isFirstLunch(context)
        viewModelScope.launch {
            if (isFirst) {
                insertWordsDetector()
                insertDefaultSenders()
                insertContent()
                SharedPreferenceManager.setFirstLunch(context)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                insertSms()
            }, 3000)


        }

    }



     data class SplashUiState(
        var isLoading:Boolean = true
    )

}