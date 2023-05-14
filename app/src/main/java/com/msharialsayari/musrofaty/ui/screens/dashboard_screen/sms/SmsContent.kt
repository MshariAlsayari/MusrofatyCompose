package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.sms

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.*
import com.msharialsayari.musrofaty.ui_component.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SmsContent(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel,
    onSmsClicked: (String) -> Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsFlow?.collectAsLazyPagingItems()

    Box(modifier.fillMaxSize()) {
        when{
            uiState.isSmsPageLoading                        -> ItemLoading()
            smsList?.itemSnapshotList?.isNotEmpty() == true -> SmsList(viewModel = viewModel, list = smsList, onSmsClicked = onSmsClicked , onNavigateToSenderSmsList = onNavigateToSenderSmsList)
            else                                            -> EmptyCompose()
        }
    }



}



@Composable
fun SmsList(
    list: LazyPagingItems<SmsEntity>,
    viewModel: DashboardViewModel,
    onSmsClicked: (String) -> Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier,
        state = rememberLazyListState(),
    ) {

        itemsIndexed(key = { _, sms -> sms.id }, items = list) { index, item ->

            if (item != null) {

                SmsComponent(
                    modifier = Modifier.clickable {
                        onSmsClicked(item.id)
                    },
                    model = viewModel.wrapSendersToSenderComponentModel(item, context),
                    onSenderIconClicked = {
                        onNavigateToSenderSmsList(it)
                    },
                    onActionClicked = { model, action ->
                        when (action) {
                            SmsActionType.FAVORITE -> viewModel.favoriteSms(
                                model.id,
                                model.isFavorite
                            )
                            SmsActionType.COPY -> {
                                Utils.copyToClipboard(item.body, context)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.common_copied),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            SmsActionType.SHARE -> {
                                Utils.shareText(item.body, context)
                            }
                            SmsActionType.DELETE -> viewModel.softDelete(
                                context,
                                model.id,
                                model.isDeleted
                            )
                        }
                    })


            }


        }


    }


}


@Composable
fun ItemLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}

@Composable
fun EmptyCompose() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
    }

}
