package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import android.content.Context
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCurrentThemeOptionUseCase @Inject constructor() {

    operator fun invoke(context: Context): Int {
        return  SharedPreferenceManager.getTheme(context)
    }
}