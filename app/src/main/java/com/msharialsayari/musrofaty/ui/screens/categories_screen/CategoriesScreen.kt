package com.msharialsayari.musrofaty.ui.screens.categories_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStore
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui_component.DividerComponent.HorizontalDividerComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldComponent

@Composable
fun CategoriesScreen(categoryId:Int, onDone:()->Unit){
    val viewModel :CategoriesViewModel= hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit ){
        viewModel.getData(categoryId)
    }


    when{
        uiState.isLoading -> ProgressCompose()
        uiState.categoryWithStores != null -> PageCompose(viewModel)
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
fun PageCompose(viewModel: CategoriesViewModel){

    ConstraintLayout {
        val (fieldsContainer, listContainer, buttonsContainer) = createRefs()

        FieldCategoryDisplayNameCompose(modifier = Modifier
            .constrainAs(fieldsContainer) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

            }
            .padding(vertical = dimensionResource(id = R.dimen.default_margin16))
            ,viewModel)
        StoresLazyList(modifier = Modifier.constrainAs(listContainer) {
            top    .linkTo(fieldsContainer.bottom)
            start  .linkTo(parent.start)
            end    .linkTo(parent.end)
            bottom .linkTo(parent.bottom)
        },viewModel)
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
            textValue = arabicCategory.value,
            errorMsg = "",
            label = R.string.common_arabic,
            onValueChanged = {
                arabicCategory.value = it
            }
        )


        TextFieldComponent.BoarderTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            textValue = englishCategory.value,
            errorMsg = "",
            label = R.string.common_english,
            onValueChanged = {
                englishCategory.value = it
            }
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun StoresLazyList(modifier: Modifier=Modifier,viewModel:CategoriesViewModel){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val categoryWithStore = uiState.categoryWithStores?.collectAsState(initial = CategoryWithStore(category = null, stores = emptyList()))?.value

    val deleteAction = Action<StoreEntity>(
        { TextComponent.BodyText(text = stringResource(id = com.msharialsayari.musrofaty.R.string.common_delete)) },
        { ActionIcon(id = R.drawable.ic_delete) },
        backgroundColor = colorResource(R.color.deletAction),
        onClicked = { position, item ->

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
            onItemClicked = { item, position -> },
            isLoading = uiState.isLoading,
            startActions = listOf(deleteAction),
            loadingProgress = { ProgressBar.CircleProgressBar() },
            emptyView = { EmptyComponent.EmptyTextComponent() },

            )
    }




}


