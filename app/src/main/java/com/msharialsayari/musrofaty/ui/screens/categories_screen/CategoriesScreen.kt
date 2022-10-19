package com.msharialsayari.musrofaty.ui.screens.categories_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStore
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import com.msharialsayari.musrofaty.ui_component.DividerComponent.HorizontalDividerComponent
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen(categoryId:Int, onDone:()->Unit){
    val viewModel :CategoriesViewModel= hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit ){
        viewModel.getData(categoryId)
    }


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.CategoryScreen.title,
            )

        }
    ) { innerPadding ->
        when{
            uiState.isLoading -> ProgressCompose(modifier = Modifier.padding(innerPadding))
            uiState.categoryWithStores != null -> PageCompose(Modifier.padding(innerPadding),viewModel,onDone)
        }
    }





}

@Composable
fun ProgressCompose(modifier: Modifier=Modifier,){
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        ProgressBar.CircleProgressBar()

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageCompose(modifier: Modifier=Modifier,viewModel: CategoriesViewModel, onDone: () -> Unit){

    val coroutineScope                    = rememberCoroutineScope()
    val selectedStore = remember { mutableStateOf<StoreEntity?>(null) }
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
                onCategorySelected = {newCategoryId->
                    coroutineScope.launch {
                       handleVisibilityOfBottomSheet(sheetState, false)
                    }
                    selectedStore.value?.storeName?.let { viewModel.onUpdateStoreCategory(it,newCategoryId) }

                },
                onCreateCategoryClicked = {
                    openDialog.value = true
                    coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                },
            )

        }) {


        Column(modifier = Modifier.fillMaxSize()) {
            FieldCategoryDisplayNameCompose(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.default_margin16)), viewModel
            )

            StoresLazyList(modifier = Modifier.weight(1f), viewModel, onItemClicked = {
                coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, true) }
                selectedStore.value = it

            })

            ActionButtonsCompose(modifier = Modifier, viewModel, onDone)
        }
    }


}


@Composable
fun FieldCategoryDisplayNameCompose(modifier: Modifier=Modifier,viewModel:CategoriesViewModel){

    val uiState by viewModel.uiState.collectAsState()

    val arabicCategory = remember { mutableStateOf(uiState.arabicCategory) }
    val englishCategory = remember { mutableStateOf(uiState.englishCategory) }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextFieldComponent.BoarderTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            textValue = uiState.arabicCategory,
            errorMsg = uiState.arabicCategoryValidationModel.errorMsg,
            label = R.string.common_arabic,
            onValueChanged = {
                arabicCategory.value = it
                viewModel.onArabicCategoryChanged(it)
            }
        )


        TextFieldComponent.BoarderTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            textValue =  uiState.englishCategory,
            errorMsg = uiState.englishCategoryValidationModel.errorMsg,
            label = R.string.common_english,
            onValueChanged = {
                englishCategory.value = it
                viewModel.onEnglishCategoryChanged(it)
            }
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun StoresLazyList(modifier: Modifier=Modifier,viewModel:CategoriesViewModel, onItemClicked:(StoreEntity)->Unit){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val categoryWithStore = uiState.categoryWithStores?.collectAsState(initial = CategoryWithStore(category = null, stores = emptyList()))?.value

    val deleteAction = Action<StoreEntity>(
        { TextComponent.BodyText(text = stringResource(id = com.msharialsayari.musrofaty.R.string.common_delete)) },
        { ActionIcon(id = R.drawable.ic_delete) },
        backgroundColor = colorResource(R.color.deletAction),
        onClicked = { position, item ->
            viewModel.onDeleteStoreActionClicked(item.storeName)

        })


    Column(modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextComponent.HeaderText(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
                text = stringResource(id = R.string.stores)
                )

        TextComponent.PlaceholderText(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
            text = stringResource(id = R.string.stores_description, CategoryModel.getDisplayName(context, categoryWithStore?.category))
        )

        HorizontalDividerComponent()

        VerticalEasyList(
            list = categoryWithStore?.stores ?: emptyList(),
            view = {
                TextComponent.BodyText(
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.default_margin16)),
                    text = it.storeName
                )

            },
            dividerView = { HorizontalDividerComponent() },
            onItemClicked = { item, position ->
                onItemClicked(item)

            },
            isLoading = uiState.isLoading,
            startActions = listOf(deleteAction),
            loadingProgress = { ProgressBar.CircleProgressBar() },
            emptyView = { EmptyComponent.EmptyTextComponent() },

            )
    }




}

@Composable
fun AddCategoryDialog(viewModel: CategoriesViewModel, onDismiss:()->Unit){

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

@Composable
fun CategoryBottomSheet(viewModel: CategoriesViewModel, onCategorySelected:(Int)->Unit, onCreateCategoryClicked: ()->Unit ){
    val context                           = LocalContext.current
    val uiState                           by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()
    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.store_category,
        list = viewModel.getCategoryItems(context, categories),
        trailIcon = {
            Icon( Icons.Default.Add, contentDescription =null, modifier = Modifier.clickable {
                onCreateCategoryClicked()
            } )
        },
        onSelectItem = {
               onCategorySelected(it.id)
        },


    )
}

@Composable
fun ActionButtonsCompose(modifier: Modifier=Modifier,viewModel: CategoriesViewModel, onDone: () -> Unit){
    val uiState                           by viewModel.uiState.collectAsState()

    Row (modifier = modifier){
        ButtonComponent.ActionButton(
            modifier =Modifier.weight(1f),
            text =  R.string.common_save,
            onClick = {

                if (viewModel.validate()) {
                    viewModel.onSaveBtnClicked()
                    onDone()
                }
            }

        )

            ButtonComponent.ActionButton(
                modifier =Modifier.weight(1f),
                color= R.color.deletAction,
                text =  R.string.common_delete,
                onClick = {
                    viewModel.onDeleteBtnClicked()
                    onDone()

                }

            )

    }

}


