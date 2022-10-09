package com.msharialsayari.musrofaty.ui.screens.splash_screen


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.SendersKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.*
import com.msharialsayari.musrofaty.jobs.InsertCategoryJob
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
    private val filtersRepo: FilterRepo,
    private val categoryRepo: CategoryRepo,
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

    private fun migrateForFilters(){
        viewModelScope.launch {
            filtersRepo.migrateForFilters()
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
            val sendersContent = contentRepo.getContentByKey(ContentKey.SENDERS.name)
            val banksSender =sendersContent.find{it.valueAr == SendersKey.BANKS.valueAr}
            val servicesSender =sendersContent.find{it.valueAr == SendersKey.SERVICES.valueAr}
            val digitalWalletSender =sendersContent.find{it.valueAr == SendersKey.DIGITALWALLET.valueAr}

            val senders = SenderModel.getDefaultSendersModel(
                bankContentId =banksSender?.id ?:0 ,
                servicesContentId = servicesSender?.id ?:0 ,
                digitalWalletContentId = digitalWalletSender?.id ?:0

            )
            senderRepo.insert(*senders.toTypedArray())
        }

    }

    private fun insertCategories(){
        viewModelScope.launch {
            categoryRepo.insertDefaultCategoryList()

        }
    }



    private fun initData() {
        val isFirst = SharedPreferenceManager.isFirstLunch(context)
        viewModelScope.launch {
            if (isFirst) {
                insertContent()
                insertWordsDetector()
                insertDefaultSenders()
                migrateForFilters()
                insertCategories()
                SharedPreferenceManager.setFirstLunch(context)
            }
            insertSms()



        }

    }



     data class SplashUiState(
        var isLoading:Boolean = true
    )

}