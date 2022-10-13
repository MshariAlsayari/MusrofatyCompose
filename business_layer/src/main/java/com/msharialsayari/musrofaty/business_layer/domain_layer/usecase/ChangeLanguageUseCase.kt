package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import android.content.Context
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChangeLanguageUseCase @Inject constructor() {

    operator fun invoke(context:Context,locale: Locale){
        SharedPreferenceManager.storeLanguage(context, locale)
    }
}