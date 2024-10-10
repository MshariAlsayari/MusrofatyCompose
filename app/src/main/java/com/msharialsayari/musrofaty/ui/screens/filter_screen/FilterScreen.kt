package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.AddFilterBottomSheetCompose
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.AmountFilterBottomSheetCompose
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.utils.mirror
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilterScreen() {
    val viewModel: FilterViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val selectedFilterWordItem = rememberSaveable { mutableStateOf<FilterWordModel?>(null) }
    val bottomSheetType = uiState.bottomSheetType
    val showConfirmationDialog = uiState.showConfirmationDialog

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(
                sheetState,
                false
            )
        }

    }

    LaunchedEffect(key1 = sheetState.currentValue) {
        if (sheetState.currentValue == ModalBottomSheetValue.Hidden) {
            viewModel.openBottomSheet(null)
            keyboardController?.hide()
        }
    }

    LaunchedEffect(key1 = bottomSheetType) {
        val showBottomSheet = bottomSheetType != null
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(
                sheetState,
                showBottomSheet
            )
        }
        keyboardController?.hide()
    }


    if(showConfirmationDialog){
        DialogComponent.ConfirmationDialog(
            title = stringResource(id = R.string.delete_dialog_title),
            message = stringResource(id = R.string.delete_dialog_message),
            onClickPositiveBtn = {
                viewModel.onDeleteBtnClicked()
                viewModel.navigateUp()
            },
            onClickNegativeBtn = {
                viewModel.updateConfirmationDialogStatus(false)
            }
        )
    }
    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            when (bottomSheetType) {
                FilterBottomSheetType.WORD -> AddFilterBottomSheetCompose(item = selectedFilterWordItem.value) { item, newString ->
                    viewModel.addFilter(item, newString.trim())
                    selectedFilterWordItem.value = null
                    viewModel.openBottomSheet(null)
                }

                FilterBottomSheetType.AMOUNT -> AmountFilterBottomSheetCompose(item = uiState.filterAmountModel) { item, newString ->
                    viewModel.addAmountFilter(item, newString.trim())
                    viewModel.openBottomSheet(null)
                }

                null -> {}
            }

        },
    ) {

        Scaffold(
            topBar = {
                FilterScreenTopBar(viewModel = viewModel)
            },
            scaffoldState = scaffoldState
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = dimensionResource(R.dimen.btn_height60))
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilterTitle(viewModel)
                    FiltersInstructions()
                    FilterAmountSection(viewModel = viewModel)
                    FilterWordSection(viewModel = viewModel) {
                        selectedFilterWordItem.value = it
                    }
                }

                BtnAction(modifier = Modifier.align(Alignment.BottomCenter), viewModel = viewModel)
            }

        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FiltersInstructions() {

    var openInstructionDialog by rememberSaveable { mutableStateOf(false) }

    if (openInstructionDialog) {
        Dialog(onDismissRequest = {
            openInstructionDialog = false
        }) {
            Card(modifier = Modifier) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_one_click))
                    TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_double_click))
                    TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_slide))
                    TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_and))
                    TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_or))

                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            openInstructionDialog = false
                        }) {
                        Text(text = stringResource(id = R.string.common_ok))
                    }
                }
            }
        }

    }

    SectionHeader(
        title = stringResource(id = R.string.common_instructions),
        icon = Icons.Default.Info,
        withDivider = false
    ) {
        openInstructionDialog = true
    }


}

@Composable
fun FilterTitle(viewModel: FilterViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val title = uiState.filterModel.title
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = title,
        label = R.string.filter_title,
        errorMsg = uiState.titleValidationModel.errorMsg,
        onValueChanged = {
            viewModel.onFilterTitleChanged(it)
        }
    )

}


@ExperimentalComposeUiApi
@Composable
fun SectionHeader(
    title: String,
    icon: ImageVector? = null,
    withDivider: Boolean = true,
    onIconClicked: () -> Unit
) {


    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_margin16)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextComponent.HeaderText(text = title)
            if (icon != null)
                Icon(icon, contentDescription = null, modifier = Modifier.clickable {
                    onIconClicked()
                })
        }

        if (withDivider)
            DividerComponent.HorizontalDividerComponent()


    }

}


@Composable
fun BtnAction(
    modifier: Modifier = Modifier,
    viewModel: FilterViewModel
) {
    val uiState by viewModel.uiState.collectAsState()


    ButtonComponent.ActionButton(
        modifier = modifier.fillMaxWidth(),
        text = if (uiState.isCreateNewFilter) R.string.common_create else R.string.common_save,
        onClick = {
            if (viewModel.validate()) {
                viewModel.onCreateBtnClicked()
                viewModel.navigateUp()
            }
        }

    )
}

@Composable
fun EmptyCompose() {
    Box(contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent()
    }

}



