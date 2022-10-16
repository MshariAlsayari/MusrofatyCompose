package com.msharialsayari.musrofaty.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private var isDark = false

    var _binding: ViewBinding? = null

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun activityTransition() {
//        if (SharedPreferenceManager.shouldUseDifferentActivityTransition(this)){
//            // transition with finish activities ex:change language
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//            SharedPreferenceManager.setShouldUseDifferentActivityTransition(this, false)
//        }else {
//            // transition between activities
//            if (SharedPreferenceManager.isArabic(this))
//                overridePendingTransition(R.anim.enter_ar, R.anim.exit_ar)
//            else
//                overridePendingTransition(R.anim.enter, R.anim.exit)
//        }
    }



    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(SharedPreferenceManager.applyLanguage(newBase, SharedPreferenceManager.getLanguage(newBase)))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (SharedPreferenceManager.isArabic(this))
            overridePendingTransition(R.anim.left_to_right_ar, R.anim.right_to_left_ar)
        else
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun setTheme(){
//
//        var currentTheme = SharedPreferenceManager.getTheme(this)
//
//        if (currentTheme == -99){
//            currentTheme =  when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
//                UI_MODE_NIGHT_YES -> R.style.darkTheme
//                UI_MODE_NIGHT_NO -> R.style.lightTheme
//                else -> R.style.lightTheme
//            }
//        }
//
//
//        setTheme(currentTheme)
//        SharedPreferenceManager.setTheme(this,currentTheme)

    }




}