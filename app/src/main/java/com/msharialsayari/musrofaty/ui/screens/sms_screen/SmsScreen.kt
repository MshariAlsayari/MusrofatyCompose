package com.msharialsayari.musrofaty.ui.screens.sms_screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun SmsScreen(smsId:String, onNavigateToCategoryScreen:(Int)->Unit,onBackPressed:()->Unit){
    val viewModel:SmsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit ){
        viewModel.getData(smsId)
    }


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SmsScreen.title,
                onArrowBackClicked = onBackPressed
            )

        }
    ) { innerPadding ->
        when{
            uiState.isLoading -> ProgressCompose(modifier = Modifier.padding(innerPadding))
            uiState.sms != null -> PageCompose(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onCategoryLongPressed ={
                    onNavigateToCategoryScreen(it)
                })
        }
    }



}

@Composable
fun ProgressCompose(modifier: Modifier=Modifier){
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        ProgressBar.CircleProgressBar()

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageCompose(modifier: Modifier=Modifier,viewModel: SmsViewModel, onCategoryLongPressed:(Int)->Unit){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val sms     = uiState.sms!!
    val coroutineScope                    = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val sheetState = rememberBottomSheetScaffoldState()


    BackHandler(sheetState.bottomSheetState.isExpanded) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (openDialog.value){
        AddCategoryDialog(viewModel, onDismiss = {
            openDialog.value = false
        })
    }

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
                CategoryBottomSheet(
                    viewModel =viewModel,
                    onCategorySelected = {
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, false)
                        }
                        viewModel.onCategoryChanged()

                    },
                    onCreateCategoryClicked = {
                        openDialog.value = true
                        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                    },
                    onCategoryLongPressed = {filterId->
                        onCategoryLongPressed(filterId)
                        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                    }
                )

        }) {


        Box(modifier = Modifier.fillMaxSize()) {
            SmsComponent(
                model = viewModel.wrapSendersToSenderComponentModel(sms, context),
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
                            Utils.copyToClipboard(model.body,context)
                            Toast.makeText(context, context.getString(R.string.common_copied), Toast.LENGTH_SHORT).show()
                        }

                        SmsActionType.ShARE -> {
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

@Composable
fun CategoryBottomSheet(viewModel: SmsViewModel, onCategorySelected:()->Unit, onCreateCategoryClicked: ()->Unit, onCategoryLongPressed:(Int)->Unit ){
    val context                           = LocalContext.current
    val uiState                           by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()
    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.store_category,
        description= R.string.common_long_click_to_modify,
        list = viewModel.getCategoryItems(context, categories),
        trailIcon = {
            Icon( Icons.Default.Add, contentDescription =null, modifier = Modifier.clickable {
                onCreateCategoryClicked()
            } )
        },
        canUnSelect = true,
        onSelectItem = {
            if (it.isSelected) {
                uiState.selectedCategory = it
            }else{
                uiState.selectedCategory = null
            }
            onCategorySelected()
        },
        onLongPress = {
            onCategoryLongPressed(it.id)
        }


    )
}

@Composable
fun AddCategoryDialog(viewModel: SmsViewModel, onDismiss:()->Unit){

    Dialog(onDismissRequest = onDismiss) {

        DialogComponent.AddCategoryDialog(
            onClickPositiveBtn = {ar,en->
                viewModel.addCategory(CategoryModel(
                    valueEn = en,
                    valueAr = ar,
                ))

                onDismiss()

            },
            onClickNegativeBtn = onDismiss
        )

    }

}



