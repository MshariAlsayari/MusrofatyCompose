package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import android.content.Context
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangeThemeUseCase @Inject constructor() {

    operator fun invoke(context: Context, appTheme: AppTheme){
        SharedPreferenceManager.storeTheme(context, appTheme)
    }
}