package com.msharialsayari.musrofaty.ui.screens.sms_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui_component.*
import kotlinx.coroutines.launch

@Composable
fun SmsScreen(smsId:String){
    val viewModel:SmsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit ){
        viewModel.getData(smsId)
    }

    when{
        uiState.isLoading -> ProgressCompose()
        uiState.sms != null -> PageCompose(viewModel,uiState.sms!!)
    }

}

@Composable
fun ProgressCompose(){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        ProgressBar.CircleProgressBar()

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageCompose(viewModel: SmsViewModel, sms:SmsModel){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope                    = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (openDialog.value){
        AddCategoryDialog(viewModel, onDismiss = {
            openDialog.value = false
        })
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
                CategoryBottomSheet(
                    viewModel =viewModel,
                    onCategorySelected = {
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                        }
                        viewModel.onCategoryChanged()

                    },
                    onCreateCategoryClicked = {
                        openDialog.value = true
                        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                    },
                    onCategoryLongPressed = {filterId->

                    }
                )

        }) {


        Box(modifier = Modifier.fillMaxSize()) {
            SmsComponent(
                model = viewModel.wrapSendersToSenderComponentModel(sms, context),
                onCategoryClicked = {
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                    }
                },
                onActionClicked = { model, action ->
                    when (action) {
                        SmsActionType.FAVORITE -> viewModel.favoriteSms(
                            model.id,
                            model.isFavorite
                        )
                        SmsActionType.COPY -> {}
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
        title = R.string.sender_category,
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
                    isDefault = false,
                ))

                onDismiss()

            },
            onClickNegativeBtn = onDismiss
        )

    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
suspend fun handleVisibilityOfBottomSheet(sheetState: ModalBottomSheetState, show: Boolean) {

    if (show) {
        sheetState.show()
    } else {
        sheetState.hide()
    }

}



