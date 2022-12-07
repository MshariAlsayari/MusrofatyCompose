package com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.TextComponent


@Composable
fun StoreSmsListScreen(storeName:String, onBackPressed:()->Unit){

    val viewModel: StoreSmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getAllSms(storeName)
    }


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = storeName,
                onArrowBackClicked = onBackPressed
            )

        }
    ) { innerPadding ->

        when{
            uiState.isLoading       -> PageLoading(Modifier.padding(innerPadding))
            uiState.smsFlow != null -> PageCompose(Modifier.padding(innerPadding), viewModel)
            else                    -> PageEmpty(Modifier.padding(innerPadding))
        }

    }



}


@Composable
fun PageCompose(modifier: Modifier=Modifier, viewModel: StoreSmsListViewModel){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsFlow?.collectAsLazyPagingItems()



    val listState  = rememberLazyListState()

    if (smsList?.itemSnapshotList?.isNotEmpty() == true) {

        LazyColumn(
            modifier = modifier,
            state = listState,
        ) {

            itemsIndexed(key = { _, sms -> sms.id }, items = smsList) { index, item ->

                if (item != null) {
                    TextComponent.BodyText(
                        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.default_margin16)),
                        text = item.body
                    )

                    if (smsList.itemSnapshotList.lastIndex != index){
                        DividerComponent.HorizontalDividerComponent()

                    }
                }


            }


        }
    }else{
        PageEmpty(modifier)
    }

}


@Composable
fun PageLoading(modifier: Modifier=Modifier){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}

@Composable
fun PageEmpty(modifier: Modifier=Modifier){
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