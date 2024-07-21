package com.msharialsayari.musrofaty.base

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.getColorFromAttr
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
        setTheme()
        setStatusBarColor()
    }

    private fun setStatusBarColor() {
        val color = getColorFromAttr(R.attr.colorStatusBar)
        window.statusBarColor = color
    }



    override fun onBackPressed() {
        super.onBackPressed()
        if (SharedPreferenceManager.isArabic(this))
            overridePendingTransition(R.anim.left_to_right_ar, R.anim.right_to_left_ar)
        else
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun setTheme(){

        var currentTheme = SharedPreferenceManager.getTheme(this)

        currentTheme = when(currentTheme){
            1->R.style.lightTheme
                2->R.style.darkTheme
            0-> when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
                UI_MODE_NIGHT_YES -> R.style.darkTheme
                UI_MODE_NIGHT_NO -> R.style.lightTheme
                else -> R.style.lightTheme
            }
            else -> {R.style.darkTheme}
        }

        setTheme(currentTheme)


    }




}