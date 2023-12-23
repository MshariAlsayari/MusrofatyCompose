package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.appbar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.rememberAllSmsState
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.SenderComponent
import com.msharialsayari.musrofaty.ui_component.SenderComponentModel
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.singleMediaPicker
import com.msharialsayari.musrofaty.utils.DateUtils

@Composable
fun SenderSmsListExpandedBar(viewModel: SenderSmsListViewModel) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UpperPartExpandedToolbar(viewModel)
        DividerComponent.HorizontalDividerComponent()
        LowerPartExpandedToolbar(viewModel)
    }
}



@Composable
fun UpperPartExpandedToolbar(viewModel: SenderSmsListViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val sender = uiState.sender
    val smsCount = rememberAllSmsState(viewModel).value.size
    val model = SenderComponentModel(
        senderId = sender.id,
        senderName = sender.senderName,
        senderIconPath = sender.senderIconUri,
        displayName = SenderModel.getDisplayName(context, sender),
        senderType = ContentModel.getDisplayName(context, sender.content),
    )

    val corpOption = CropImageOptions()
    corpOption.cropShape = CropImageView.CropShape.OVAL
    corpOption.allowFlipping = false
    corpOption.allowRotation = false
    corpOption.scaleType = CropImageView.ScaleType.CENTER_CROP


    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            viewModel.updateSenderIcon(result.uriContent.toString())
        }
    }

    val galleryLauncher = singleMediaPicker(onSelectMedia = {
        val cropOptions = CropImageContractOptions(it, corpOption)
        imageCropLauncher.launch(cropOptions)
    })



    SenderComponent(
        modifier = Modifier.padding(horizontal = 16.dp),
        model = model
    ) {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Column {
            TextComponent.PlaceholderText(
                text = stringResource(id = R.string.common_sender_shortcut) + ": " + sender?.senderName
            )

            TextComponent.PlaceholderText(
                text = stringResource(id = R.string.common_sms_total) + ": " + smsCount.toString() + " " + pluralStringResource(
                    id = R.plurals.common_sms,
                    count = smsCount
                )
            )

        }

        ButtonComponent.OutlineButton(
            text = R.string.common_details,
            onClick = {
                viewModel.navigateToSenderDetails(sender?.id ?: 0)
            }
        )

    }

}

@Composable
fun LowerPartExpandedToolbar(viewModel: SenderSmsListViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val filters = uiState.filters
    val selectedTimeFilter = uiState.selectedFilterTimeOption
    val selectedFilter = uiState.selectedFilter

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {


        TextComponent.PlaceholderText(
            text = stringResource(id = R.string.common_filter_options) + ": " + (selectedTimeFilter?.value
                ?: stringResource(id = DateUtils.FilterOption.ALL.title))
        )


        if (filters.isEmpty()) {

            Row(horizontalArrangement = Arrangement.SpaceAround) {
                TextComponent.PlaceholderText(
                    text = stringResource(id = R.string.common_filter) + ": "
                )

                TextComponent.ClickableText(
                    modifier = Modifier.clickable {
                        uiState.sender?.let {
                            viewModel.navigateToFilterScreen(it.id, null)
                        }
                    },
                    text = stringResource(id = R.string.create_filter)
                )
            }

        } else {
            TextComponent.PlaceholderText(
                text = stringResource(id = R.string.common_filter) + ": " + (selectedFilter?.value
                    ?: stringResource(id = R.string.common_no_selected))
            )
        }


    }

}