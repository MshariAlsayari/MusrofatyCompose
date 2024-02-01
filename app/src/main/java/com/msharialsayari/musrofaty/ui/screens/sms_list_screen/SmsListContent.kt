package com.msharialsayari.musrofaty.ui.screens.sms_list_screen

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.bottomSheet.SmsListBottomSheetType
import com.msharialsayari.musrofaty.ui_component.SmsActionType
import com.msharialsayari.musrofaty.ui_component.SmsComponent
import com.msharialsayari.musrofaty.ui_component.wrapSendersToSenderComponentModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SmsListContent(modifier: Modifier = Modifier,
                   viewModel: SmsListViewModel,
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsList
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {

        items(items = smsList) { item ->
            SmsComponent(
                model = wrapSendersToSenderComponentModel(
                    item,
                    context
                ),
                onCategoryClicked = {
                    viewModel.onSmsCategoryClicked(item)
                    viewModel.updateSelectedBottomSheet(SmsListBottomSheetType.CATEGORIES)
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
                            model.id,
                            model.isDeleted
                        )
                    }
                })

        }


    }

}