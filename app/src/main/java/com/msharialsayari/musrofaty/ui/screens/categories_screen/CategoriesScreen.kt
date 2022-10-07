package com.msharialsayari.musrofaty.ui.screens.categories_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui_component.*

@Composable
fun CategoriesScreen(storeName:String, categoryId:Int?, onDone:()->Unit){
    val viewModel :CategoriesViewModel= hiltViewModel()
    Column(modifier = Modifier.fillMaxSize()) {
        CategoriesLazyList(viewModel,categoryId)
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun CategoriesLazyList(viewModel:CategoriesViewModel, selectedCategoryId :Int?){
    val uiState by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()

    val deleteAction = Action<CategoryEntity>(
        { TextComponent.BodyText(text = stringResource(id = com.msharialsayari.musrofaty.R.string.common_delete)) },
        { ActionIcon(id = com.msharialsayari.musrofaty.R.drawable.ic_delete) },
        backgroundColor = colorResource(com.msharialsayari.musrofaty.R.color.deletAction),
        onClicked = { position, item ->

        })


    VerticalEasyList(
        list = categories,
        view = {
            CategoryComposable(it,selectedCategoryId, onSelect = {

            })

        },
        dividerView = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked = { item, position ->



        },
        isLoading = uiState.isLoading,
        startActions = listOf(deleteAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView = { EmptyComponent.EmptyTextComponent() },

    )

}

@Composable
fun CategoryComposable(categoryEntity: CategoryEntity, selectedCategoryId :Int?, onSelect:(Int)->Unit){
val context = LocalContext.current
    RadioButtonComponent(
        text = CategoryModel.getDisplayName(context, categoryEntity),
        isSelected= categoryEntity.id == selectedCategoryId,
        onSelected = {
            onSelect(categoryEntity.id)
    })

}

