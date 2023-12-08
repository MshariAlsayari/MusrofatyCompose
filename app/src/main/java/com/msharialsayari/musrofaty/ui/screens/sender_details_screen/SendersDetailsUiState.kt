package com.msharialsayari.musrofaty.ui.screens.sender_details_screen

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel

data class SendersDetailsUiState(
    val sender: SenderModel? = null,
    val isPin: Boolean = false,
    var contents: List<ContentModel> = emptyList()
) {


    fun changeArabicDisplayName(name: String): SenderModel? {
        val model = sender
        model?.displayNameAr = name
        return model

    }

    fun changeEnglishDisplayName(name: String): SenderModel? {
        val model = sender
        model?.displayNameEn = name
        return model

    }

    fun wrapContentModel(context: Context): List<SelectedItemModel> {
        val list = mutableListOf<SelectedItemModel>()
        this.contents.forEach {
            val model = SelectedItemModel(
                id = it.id,
                value = ContentModel.getDisplayName(context, it),
                isSelected = sender?.content?.id == it.id
            )
            list.add(model)
        }
        return list.toList()

    }

    fun updateSenderCategory(contentId: Int): SenderModel? {
        val model = sender
        model?.content = contents.find { it.id == contentId }
        model?.contentId = contentId
        return model

    }

}