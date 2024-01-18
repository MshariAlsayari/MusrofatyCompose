package com.msharialsayari.musrofaty.ui.screens.statistics_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.sender_details_screen.ClickableTextListItemCompose
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets.BottomSheetType
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets.DateRangeBottomSheet
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets.SelectCategoryBottomSheet
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets.TimePeriodsBottomSheet
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar.CircleProgressBar
import kotlinx.coroutines.launch

@Composable
fun StatisticsScreen() {

    val viewModel: StatisticsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.selectedCategory, key2 = uiState.selectedTimePeriod){
        viewModel.getSmsList()
    }

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.StatisticsScreen.title,
                onArrowBackClicked = {viewModel.navigateUp()}
            )

        }
    ) { innerPadding ->
        Box (
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ){
            if(uiState.loading){
                CircleProgressBar()
            }else{
                StatisticsContent(modifier = Modifier, viewModel = viewModel)
            }
        }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StatisticsContent(modifier: Modifier=Modifier, viewModel: StatisticsViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetType = uiState.bottomSheetType
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
    }


    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            if(bottomSheetType != null)
            when (bottomSheetType) {
                BottomSheetType.CATEGORIES->   SelectCategoryBottomSheet(viewModel,sheetState)
                BottomSheetType.TIME_PERIODS-> TimePeriodsBottomSheet(viewModel,sheetState)
                BottomSheetType.DATE_PICKER -> DateRangeBottomSheet(viewModel,sheetState)
            }
        }) {

        Column (
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            CategoryItem(viewModel){
                viewModel.updateBottomSheetType(BottomSheetType.CATEGORIES)
                coroutineScope.launch {
                    BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                }
            }

            PeriodItem(viewModel){
                viewModel.updateBottomSheetType(BottomSheetType.TIME_PERIODS)
                coroutineScope.launch {
                    BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                }
            }
        }


    }


}

@Composable
private fun CategoryItem(viewModel: StatisticsViewModel, onClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()

    val selectedItem = uiState.selectedCategory
    val categories = viewModel.observingCategories().collectAsState(initial = emptyList())

    ClickableTextListItemCompose(
        title = stringResource(id = R.string.category),
        value = viewModel.getSelectedCategory(selectedItem, categories.value),
        onClick = onClick)

}

@Composable
private fun PeriodItem(viewModel: StatisticsViewModel, onClick:()->Unit){
    val value =  viewModel.getFilterTimeOption()

    ClickableTextListItemCompose(
        title = stringResource(id = R.string.common_period_time),
        value = value,
        onClick = onClick)

}

