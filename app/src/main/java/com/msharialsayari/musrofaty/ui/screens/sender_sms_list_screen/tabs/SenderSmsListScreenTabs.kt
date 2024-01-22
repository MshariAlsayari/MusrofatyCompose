package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.annotation.StringRes
import com.msharialsayari.musrofaty.R

enum class SenderSmsListScreenTabs(val index: Int, @StringRes val title: Int){
    ALL(0, R.string.tab_all_sms),
    FAVORITE(1, R.string.tab_favorite_sms),
    DELETED(2, R.string.tab_deleted_sms),
    FINANCIAL(3, R.string.tab_financial_statistics),
    CATEGORIES(4, R.string.tab_categories_statistics);

    companion object{
        fun getTabByIndex(index:Int):SenderSmsListScreenTabs{
            return when(index){
                1->FAVORITE
                2->DELETED
                3->FINANCIAL
                4->CATEGORIES
                else->ALL
            }

        }

    }



}