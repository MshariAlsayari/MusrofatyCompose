package com.msharialsayari.musrofaty.utils

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes

object DialogsUtils {

    fun showDialog(context: Context, params: Params){

        val builder = AlertDialog.Builder(context)

        with(builder) {

            params.title?.let {title->
                setTitle(context.getString(title))
            }

            setMessage(context.getString(params.message))


            setPositiveButton(context.getString(params.positiveBtnText)) { i, o ->
                params.positiveBtnListener()
            }

            params.negativeBtnText?.let {negativeBtnText->
                setNegativeButton(negativeBtnText) { i, o ->
                    params.negativeBtnListener?.invoke()
                }
            }

            setCancelable(params.isCancelable)


            show()
        }


    }

    data class Params(
        @StringRes val title:Int?=null,
        @StringRes val message:Int,
        @StringRes val positiveBtnText:Int,
        @StringRes val negativeBtnText:Int?=null,
        val positiveBtnListener:()->Unit = {},
        val negativeBtnListener:(()->Unit)? = null,
        val isCancelable:Boolean = false,
    )
}