package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.msharialsayari.musrofaty.utils.DateUtils


@Composable
fun SmsComponent(
    modifier: Modifier = Modifier,
    model: SmsComponentModel,
    onActionClicked: (SmsComponentModel, SmsActionType) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        SenderInfoComponent(model.senderDisplayName, model.senderCategory, model.senderIcon)
        SmsBodyComponent(model.body)
        SmsInfoRowComponent(model)
        SmsActionRowComponent(model, onActionClicked = { model, action ->
            onActionClicked(model, action)
        })
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SenderInfoComponent(
    senderName: String,
    senderCategory: String,
    senderIcon: Int? = null
) {
    ListItem(
        text = { Text(text = senderName) },
        secondaryText = { Text(text = senderCategory) },
        icon = {
            senderIcon?.let {
                AvatarComponent(it)
            }
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
        modifier=Modifier.padding(top = dimensionResource(id = R.dimen.default_margin16), start = dimensionResource(id = R.dimen.default_margin16), end = dimensionResource(id = R.dimen.default_margin16)),
        text = DateUtils.getHowLongPostDate(context , model.timestamp)
    )

}

@Composable
private fun SmsActionRowComponent(
    model: SmsComponentModel,
    onActionClicked: (SmsComponentModel, SmsActionType) -> Unit
) {
    val isFavoriteState = remember { mutableStateOf(model.isFavorite) }

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
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = if (isFavoriteState.value) Color.Red else colorResource(id = R.color.lightGray)
            )
            Icon(
                modifier = Modifier.clickable {
                    onActionClicked(model, SmsActionType.SHARE)
                },
                tint = colorResource(id = R.color.lightGray),
                imageVector = Icons.Outlined.Share,
                contentDescription = null
            )

        }

    }


}

enum class SmsActionType {
    FAVORITE, SHARE

}

data class SmsComponentModel(
    var id: String,
    var timestamp: Long = 0,
    var body: String = "",
    var smsType: String = "",
    var currency: String = "",
    var senderDisplayName: String = "",
    var senderCategory: String = "",
    var senderIcon: Int? = null,
    var isFavorite: Boolean = false
)
