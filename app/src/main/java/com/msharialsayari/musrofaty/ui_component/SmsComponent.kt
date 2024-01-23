package com.msharialsayari.musrofaty.ui_component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toSmsComponentModel
import com.msharialsayari.musrofaty.utils.DateUtils


@Composable
fun SmsComponent(
    modifier: Modifier = Modifier,
    model: SmsComponentModel,
    forceHideStoreAndCategory: Boolean = false,
    onSmsClicked:((String)->Unit)? = null,
    onActionClicked: (SmsComponentModel, SmsActionType) -> Unit,
    onCategoryClicked: (String) -> Unit = {},
    onSenderIconClicked:(Int) ->Unit = {}
) {

    val parentModifier = if(onSmsClicked == null){
        modifier
    }else{
        modifier.clickable {
            onSmsClicked(model.id)
        }
    }
    Column(
        modifier = parentModifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        SenderInfoComponent(model.senderId, model.senderDisplayName, model.senderCategory, model.senderIcon, onSenderIconClicked)
        SmsBodyComponent(model.body)
        if (!forceHideStoreAndCategory)
            StoreAndCategoryComponent(model, onCategoryClicked = onCategoryClicked)
        SmsInfoRowComponent(model)
        SmsActionRowComponent(model, onActionClicked = { model, action ->
            onActionClicked(model, action)
        })
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SenderInfoComponent(
    senderId: Int,
    senderName: String,
    senderCategory: String,
    senderIcon: String = "",
    onIconClicked:(Int)->Unit
) {
    ListItem(
        text = { Text(text = senderName) },
        secondaryText = { Text(text = senderCategory) },
        icon = {
            AvatarComponent(senderIcon , onClicked = {
                onIconClicked(senderId)
            })
        }
    )

}

@Composable
private fun SmsBodyComponent(body: String) {
    TextComponent.BodyText(
        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
        text = body
    )

}

@Composable
private fun SmsInfoRowComponent(
    model: SmsComponentModel,
){
    val context = LocalContext.current
    TextComponent.PlaceholderText(
        modifier = Modifier.padding(
            top = dimensionResource(id = R.dimen.default_margin16),
            start = dimensionResource(id = R.dimen.default_margin16),
            end = dimensionResource(id = R.dimen.default_margin16)
        ),
        text = DateUtils.getHowLongPostDate(context, model.timestamp)
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StoreAndCategoryComponent(
    model: SmsComponentModel,
    onCategoryClicked:(String)->Unit
){

    Column(modifier = Modifier.fillMaxWidth()) {

        if (model.storeName.isNotEmpty()) {
            ListItem(
                text = { Text(text = stringResource(id = R.string.store)) },
                trailing = {
                    TextComponent.PlaceholderText(
                        text = model.storeName,
                    )
                }
            )
        }

        if (model.storeName.isNotEmpty()) {
            DividerComponent.HorizontalDividerComponent()
            ListItem(
                text = { Text(text = stringResource(id = R.string.category)) },
                trailing = {
                    TextComponent.ClickableText(
                        text = model.storeCategory,
                        modifier = Modifier.clickable {
                            onCategoryClicked(model.storeCategory)
                        })
                }
            )
        }





    }



}

@Composable
private fun SmsActionRowComponent(
    model: SmsComponentModel,
    onActionClicked: (SmsComponentModel, SmsActionType) -> Unit
) {
    val isFavoriteState = remember { mutableStateOf(model.isFavorite) }
    val isDeletedState = remember { mutableStateOf(model.isDeleted) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.default_margin16))
    ) {

        DividerComponent.HorizontalDividerComponent()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_margin16)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Icon(
                modifier = Modifier.clickable {
                    isFavoriteState.value = !isFavoriteState.value
                    model.isFavorite = isFavoriteState.value
                    onActionClicked(model, SmsActionType.FAVORITE)
                },
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = if (isFavoriteState.value) colorResource(id = R.color.secondary_color) else colorResource(id = R.color.light_gray)
            )
            Icon(
                modifier = Modifier.clickable {
                    onActionClicked(model, SmsActionType.COPY)
                },
                tint = colorResource(id = R.color.light_gray),
                painter = painterResource(id = R.drawable.ic_copy),
                contentDescription = null
            )


            Icon(
                modifier = Modifier.clickable {
                    onActionClicked(model, SmsActionType.SHARE)
                },
                tint = colorResource(id = R.color.light_gray),
                imageVector = Icons.Outlined.Share,
                contentDescription = null
            )


            Icon(
                modifier = Modifier.clickable {
                    isDeletedState.value = !isDeletedState.value
                    model.isDeleted = isDeletedState.value
                    onActionClicked(model, SmsActionType.DELETE)
                },
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = if (isDeletedState.value) colorResource(id = R.color.expenses_color) else colorResource(id = R.color.light_gray)
            )

        }
        DividerComponent.HorizontalDividerComponent()

    }


}

fun wrapSendersToSenderComponentModel(
    sms: SmsEntity,
    sender:SenderModel,
    context: Context
): SmsComponentModel {

    return SmsComponentModel(
        id = sms.id,
        senderId = sms.senderId,
        timestamp = sms.timestamp,
        isFavorite = sms.isFavorite,
        isDeleted = sms.isDeleted,
        body = sms.body,
        senderIcon = sender.senderIconUri,
        senderDisplayName = SenderModel.getDisplayName(context, sender),
        senderCategory = ContentModel.getDisplayName(context, sender.content)
    )

}

fun wrapSendersToSenderComponentModel(
    sms: SmsModel,
    context: Context
): SmsComponentModel {

    return sms.toSmsComponentModel(context)

}

enum class SmsActionType {
    FAVORITE, COPY,SHARE,DELETE

}

data class SmsComponentModel(
    var id: String, //SMS
    var senderId: Int,
    var timestamp: Long = 0,
    var body: String = "",
    var smsType: String = "",
    var currency: String = "",
    var senderDisplayName: String = "",
    var senderCategory: String = "",
    var senderIcon: String = "",
    var isFavorite: Boolean = false,
    var isDeleted: Boolean = false,
    var storeName: String = "",
    var storeCategory: String = "",
)
