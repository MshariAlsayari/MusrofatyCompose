package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource


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
private fun SmsActionRowComponent(
    model: SmsComponentModel,
    onActionClicked: (SmsComponentModel, SmsActionType) -> Unit
) {
    val isFavoriteState = remember { mutableStateOf(model.isFavorite) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(id = R.dimen.default_margin16))
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
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = if (isFavoriteState.value) Color.Red else LocalContentColor.current.copy(
                    alpha = LocalContentAlpha.current
                )
            )
            Icon(
                modifier = Modifier.clickable {
                    onActionClicked(model, SmsActionType.SHARE)
                },
                imageVector = Icons.Default.Share,
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
