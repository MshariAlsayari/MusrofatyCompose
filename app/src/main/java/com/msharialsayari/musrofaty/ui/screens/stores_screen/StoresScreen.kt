package com.msharialsayari.musrofaty.ui.screens.stores_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.utils.notEmpty

@Composable
fun StoresScreen() {
    val viewModel: StoresViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    when {
        uiState.isLoading -> PageLoading()
        else -> PageCompose(viewModel)
    }
}

@Composable
fun PageLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}

@Composable
fun PageCompose(viewModel: StoresViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    StoresList(viewModel)
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun StoresList(viewModel: StoresViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val stores = uiState.stores?.collectAsState(initial = emptyList())?.value ?: emptyList()

    VerticalEasyList(
        list = stores,
        view = { StoreAndCategoryCompose(it) },
        onItemClicked = { item, position -> },
        dividerView = { DividerComponent.HorizontalDividerComponent() }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreAndCategoryCompose(item: StoreWithCategory) {
    val context = LocalContext.current
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.default_margin16)),
        text = { Text(text = item.store.storeName) },
        trailing = {
            TextComponent.ClickableText(
                text = if (CategoryModel.getDisplayName(context, item.category)
                        .notEmpty()
                ) CategoryModel.getDisplayName(
                    context,
                    item.category
                ) else context.getString(R.string.common_no_category),

                )
        }
    )

}