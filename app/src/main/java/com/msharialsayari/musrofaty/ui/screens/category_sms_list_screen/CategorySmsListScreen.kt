package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.SmsActionType
import com.msharialsayari.musrofaty.ui_component.SmsComponent
import com.msharialsayari.musrofaty.ui_component.wrapSendersToSenderComponentModel

@Composable
fun CategorySmsListScreen() {
    val viewModel: CategorySmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val category = uiState.category


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                titleString = CategoryModel.getDisplayName(context, category),
                onArrowBackClicked = { viewModel.navigateUp() }
            )

        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> PageLoading(modifier = Modifier.padding(innerPadding))
            uiState.smsList.isNotEmpty() -> PageCompose(Modifier.padding(innerPadding), viewModel)
            else -> PageEmpty(
                Modifier.padding(
                    innerPadding
                )
            )
        }
    }

}


@Composable
fun PageLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}

@Composable
private fun PageCompose(modifier: Modifier = Modifier, viewModel: CategorySmsListViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsList
    val listState = rememberLazyListState()


    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {

        items(items = smsList) { item ->
                SmsComponent(
                    onSmsClicked = {

                    },
                    model = wrapSendersToSenderComponentModel(
                        item,
                        context
                    ),
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

@Composable
fun PageEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmptyComponent.EmptyTextComponent()
    }
}