package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.appbar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui.toolbar.CollapsingToolbar
import com.msharialsayari.musrofaty.ui.toolbar.ToolbarState
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.shimmerBrush
import com.msharialsayari.musrofaty.utils.findActivity
import com.msharialsayari.musrofaty.utils.mirror


@Composable
fun SenderSmsListCollapsedBar(
    toolbarState: ToolbarState,
    viewModel: SenderSmsListViewModel,
    onFilterTimeIconClicked: () -> Unit,
    onFilterIconClicked: () -> Unit
){

    CollapsingToolbar(
        progress = toolbarState.progress,
        actions = {
            ToolbarActionsComposable(
                viewModel,
                onFilterTimeIconClicked,
                onFilterIconClicked,
            )
        },
        collapsedComposable = { CollapsedToolbarComposable(viewModel) },
        expandedComposable = { SenderSmsListExpandedBar(viewModel) },
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarState.height.dp + 5.dp)
    )

}

@Composable
fun CollapsedToolbarComposable(viewModel: SenderSmsListViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val smsCount =  uiState.totalSms
    val isLoading =  uiState.totalSmsLoading

    Column(modifier = Modifier.fillMaxWidth()) {
        TextComponent.HeaderText(
            text = SenderModel.getDisplayName(context, uiState.sender)
        )



        TextComponent.BodyText(
            modifier = Modifier.background(
                shimmerBrush(
                    targetValue = 1300f,
                    showShimmer = isLoading
                )
            ).width(150.dp),
            color = if (isLoading) Color.Transparent else MusrofatyTheme.colors.textBodyColor,
            text = "$smsCount " + pluralStringResource(
                id = R.plurals.common_sms,
                count = smsCount
            )
        )

    }

}


@Composable
fun ToolbarActionsComposable(
    viewModel: SenderSmsListViewModel,
    onFilterTimeIconClicked: () -> Unit,
    onFilterIconClicked: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context.findActivity()
    val isFilterSelected = uiState.selectedFilter != null
    val isFilterDateSelected = uiState.selectedFilterTimeOption != null && uiState.selectedFilterTimeOption?.id != 0
    val canGenerateFile = uiState.totalSms > 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Icon(
            Icons.Default.ArrowBack,
            tint = MusrofatyTheme.colors.iconBackgroundColor,
            contentDescription = null,
            modifier = Modifier
                .mirror()
                .clickable {
                    viewModel.navigateBack()
                })


        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {


            Icon(painter = painterResource(id = R.drawable.ic_excel),
                tint = MusrofatyTheme.colors.iconBackgroundColor,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        if (canGenerateFile) {
                            viewModel.generateExcelFile(activity)
                        } else {
                            Toast.makeText(context, context.getString(R.string.no_sms_to_generat_file), Toast.LENGTH_SHORT).show()
                        }

                    })

            Icon(painter = painterResource(id = R.drawable.ic_pdf),
                tint = MusrofatyTheme.colors.iconBackgroundColor,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        if (canGenerateFile) {
                            viewModel.navigateToPDFActivity(activity,viewModel.getPdfBundle())
                        } else {
                            Toast.makeText(context, context.getString(R.string.no_sms_to_generat_file), Toast.LENGTH_SHORT).show()
                        }

                    })





            Icon(painter = painterResource(id = R.drawable.ic_filter),
                tint = if (isFilterSelected) MusrofatyTheme.colors.secondary else MusrofatyTheme.colors.iconBackgroundColor,
                contentDescription = null,
                modifier = Modifier
                    .mirror()
                    .clickable {
                        onFilterIconClicked()
                    })

            Icon(
                Icons.Default.DateRange,
                tint = if (isFilterDateSelected) MusrofatyTheme.colors.secondary else MusrofatyTheme.colors.iconBackgroundColor,
                contentDescription = null,
                modifier = Modifier
                    .mirror()
                    .clickable {
                        onFilterTimeIconClicked()
                    })
        }


    }

}