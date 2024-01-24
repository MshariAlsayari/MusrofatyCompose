package com.msharialsayari.musrofaty.ui.screens.sms_screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.launch

@Composable
fun SmsScreen(smsId: String) {
    val viewModel: SmsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getData(smsId)
    }


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SmsScreen.title,
                onArrowBackClicked = {viewModel.navigateUp()}
            )

        }
    ) { innerPadding ->
        uiState.sms?.let {
            PageCompose(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onCategoryLongPressed = {
                    viewModel.navigateToCategoryScreen(it)
                })
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageCompose(
    modifier: Modifier = Modifier,
    viewModel: SmsViewModel,
    onCategoryLongPressed: (Int) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val sms = uiState.sms!!
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val sheetState = rememberBottomSheetScaffoldState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()


    BackHandler(sheetState.bottomSheetState.isExpanded) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (openDialog.value) {
        AddCategoryDialog(onAdd = {
            viewModel.addCategory(it)
        }, onDismiss = {
            openDialog.value = false
        })
    }

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            CategoriesBottomSheet(
                categories = viewModel.getCategoryItems(context, categories),
                onCategorySelected = {
                    viewModel.onCategorySelected(it)
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, false)
                    }
                },
                onCreateCategoryClicked = {
                    openDialog.value = true
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, false)
                    }
                },
                onCategoryLongPressed = { category ->
                    onCategoryLongPressed(category.id)
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, false)
                    }
                }
            )

        }) {


        Box(modifier = Modifier.fillMaxSize()) {
            SmsComponent(
                model = wrapSendersToSenderComponentModel(sms, context),
                onCategoryClicked = {
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, true)
                    }
                },
                onActionClicked = { model, action ->
                    when (action) {
                        SmsActionType.FAVORITE -> viewModel.favoriteSms(
                            model.id,
                            model.isFavorite
                        )

                        SmsActionType.COPY -> {
                            Utils.copyToClipboard(model.body, context)
                            Toast.makeText(
                                context,
                                context.getString(R.string.common_copied),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        SmsActionType.SHARE -> {
                            Utils.shareText(model.body, context)
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




