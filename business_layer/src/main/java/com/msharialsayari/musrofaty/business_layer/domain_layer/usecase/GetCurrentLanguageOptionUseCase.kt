package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import android.content.Context
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCurrentLanguageOptionUseCase @Inject constructor() {

    operator fun invoke(context:Context): Int {
        val language =  SharedPreferenceManager.getLanguageOption(context)
        return when(language?.language?.lowercase()){
            "ar" -> 1

            "en" -> 2

            else -> 0

        }
    }
}