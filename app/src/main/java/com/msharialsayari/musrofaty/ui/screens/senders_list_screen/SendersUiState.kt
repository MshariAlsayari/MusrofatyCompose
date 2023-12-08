package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui_component.SenderComponentModel
import kotlinx.coroutines.flow.Flow

data class SendersUiState(
    var isLoading: Boolean = false,
    var senders: Flow<List<SenderEntity>>? = null,
    ) {

        companion object {
            fun wrapSendersToSenderComponentModelList(
                senders: List<SenderEntity>,
                context: Context
            ): List<SenderComponentModel> {
                val list = mutableListOf<SenderComponentModel>()
                senders.map {
                    val model =  SenderComponentModel(
                        senderId = it.id,
                        senderName = it.senderName,
                        displayName = SenderModel.getDisplayName(context, it),
                        senderType = "",
                        senderIconPath = it.senderIconUri
                    )

                    if (it.isPined) {
                        list.add(0, model)
                    } else {
                        list.add(model)
                    }
                }
                return list

            }


        }
    }