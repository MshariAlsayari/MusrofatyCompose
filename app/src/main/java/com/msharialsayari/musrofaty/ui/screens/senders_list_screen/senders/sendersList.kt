package com.msharialsayari.musrofaty.ui.screens.senders_list_screen.senders

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListViewModel
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersUiState.Companion.wrapSendersToSenderComponentModelList
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SendersList(
    modifier: Modifier = Modifier,
    onSenderClicked: (Int) -> Unit,
    viewModel: SendersListViewModel
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val senderItems = uiState.senders?.collectAsState(emptyList())
    val screenType = MusrofatyTheme.screenType

    val pinAction = Action<SenderComponentModel>(
        {
            TextComponent.BodyText(
                text = stringResource(id = R.string.common_pin),
                color = Color.White,
                alignment = TextAlign.Center
            )
        },
        { ActionIcon(id = R.drawable.ic_pin) },
        backgroundColor = MusrofatyTheme.colors.pinActionColor,
        onClicked = { position, item ->
            viewModel.pinSender(item.senderId)
        })


    val modifyAction = Action<SenderComponentModel>(
        {
            TextComponent.BodyText(
                text = stringResource(id = R.string.common_change),
                color = Color.White,
                alignment = TextAlign.Center
            )
        },
        { ActionIcon(id = R.drawable.ic_modify) },
        backgroundColor = MusrofatyTheme.colors.modifyActionColor,
        onClicked = { position, item ->
            viewModel.navigateToSenderDetails(item.senderId)
        })


    val deleteAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_delete)) },
        { ActionIcon(id = R.drawable.ic_delete) },
        backgroundColor = MusrofatyTheme.colors.deleteActionColor,
        onClicked = { position, item ->
            viewModel.deleteSender(item.senderId)

        })


    VerticalEasyList(
        modifier = modifier,
        list = wrapSendersToSenderComponentModelList(senderItems?.value ?: emptyList(), context),
        view = { sender ->
            SenderComponent(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16)),
                model = sender,
                onAvatarClicked = {
                    if (screenType.isScreenWithDetails) {
                        onSenderClicked(it.senderId)
                    } else {
                        viewModel.navigateToSenderSmsList(senderId = it.senderId)
                    }
                }
            )
        },
        dividerView = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked = { item, position ->
            if (screenType.isScreenWithDetails) {
                onSenderClicked(item.senderId)
            } else {
                viewModel.navigateToSenderSmsList(senderId = item.senderId)
            }

        },
        isLoading = uiState.isLoading,
        endActions = listOf(modifyAction, pinAction),
        startActions = listOf(deleteAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView = { EmptyCompose() },
        onRefresh = {
            viewModel.getAllSenders()
        }
    )


}

@Composable
fun EmptyCompose() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_senders))
    }

}

@Composable
fun ActionIcon(@DrawableRes id: Int) {
    Icon(
        painter = painterResource(id = id),
        contentDescription = null,
        tint = Color.White
    )

}

