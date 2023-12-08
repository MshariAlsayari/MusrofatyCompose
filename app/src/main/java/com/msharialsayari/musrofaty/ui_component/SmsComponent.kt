package com.msharialsayari.musrofaty.ui_component

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
import com.msharialsayari.musrofaty.utils.DateUtils


@Composable
fun SmsComponent(
    modifier: Modifier = Modifier,
    model: SmsComponentModel,
    onSmsClicked:(String)->Unit = {},
    onActionClicked: (SmsComponentModel, SmsActionType) -> Unit,
    onCategoryClicked: (String) -> Unit = {},
    onSenderIconClicked:(Int) ->Unit = {}
) {
    Column(
        modifier = modifier.clickable {
            onSmsClicked(model.id)
        },
        verticalArrangement = Arrangement.SpaceAround
    ) {
        SenderInfoComponent(model.senderId, model.senderDisplayName, model.senderCategory, model.senderIcon, onSenderIconClicked)
        SmsBodyComponent(model.body)
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

        if (model.storeName.isNotEmpty() && model.storeCategory.isNotEmpty()) {
            DividerComponent.HorizontalDividerComponent()
        }


        if (model.storeCategory.isNotEmpty()) {
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
