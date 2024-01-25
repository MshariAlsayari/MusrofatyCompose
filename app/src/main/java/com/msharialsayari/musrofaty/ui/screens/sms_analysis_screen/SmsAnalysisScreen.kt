package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel
import kotlinx.coroutines.launch

@Composable
fun SmsAnalysisScreen(navigatorViewModel: AppNavigatorViewModel = hiltViewModel()){




    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SmsAnalysisScreen.title,
                onArrowBackClicked = {navigatorViewModel.navigateUp()}
            )

        }
    ) { innerPadding ->
        SmsAnalysisContent(modifier = Modifier.padding(innerPadding))
    }



}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SmsAnalysisContent(modifier: Modifier=Modifier){

    var tabIndex by remember { mutableStateOf(0) }
    val viewModel:SmsAnalysisViewModel = hiltViewModel()


    val coroutineScope                    = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (sheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                keyboardController?.hide()
            }
        }

    } else {
        DisposableEffect(Unit) {
            onDispose {
                keyboardController?.show()
            }
        }

    }

    ModalBottomSheetLayout(
        modifier=modifier,
        sheetState = sheetState,
        sheetContent = { BottomSheetComponent.TextFieldBottomSheetComponent(model = TextFieldBottomSheetModel(
            title = R.string.sms_analysis_bottom_sheet_title,
            textFieldValue =  "",
            buttonText = R.string.common_add,
            onActionButtonClicked = { value ->
                viewModel.addWordDetector(value, WordDetectorType.getById(tabIndex))
                coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
            },)
        ) }
    ){

        Box(modifier = Modifier.fillMaxSize()) {

            val tabTitles = listOf(R.string.tab_expensess, R.string.tab_income, R.string.tab_currency)
            Column {
                TabRow(
                    selectedTabIndex = tabIndex,
                    indicator = {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
                            color = MaterialTheme.colors.secondary,
                            height = TabRowDefaults.IndicatorHeight
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, stringResId ->
                        Tab(
                            modifier = Modifier.background(MaterialTheme.colors.background),
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            text = {
                                TextComponent.ClickableText(text = stringResource(id = stringResId), color = if(tabIndex == index) MaterialTheme.colors.secondary else colorResource(id = R.color.light_gray) )
                            })
                    }
                }
                when (tabIndex) {
                    0 -> SmsAnalysisTab(
                        viewModel = viewModel,
                        word = WordDetectorType.EXPENSES_PURCHASES_WORDS


                    )
                    1 -> SmsAnalysisTab(
                        viewModel = viewModel,
                        word = WordDetectorType.INCOME_WORDS
                    )

                    2 -> SmsAnalysisTab(
                        viewModel = viewModel,
                        word = WordDetectorType.CURRENCY_WORDS
                    )
                }
            }


            ButtonComponent.FloatingButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(id = R.dimen.default_margin16)),
                onClick = {
                    coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, true) }
                }
            )


        }
    }

}

@Composable
fun LoadingPageCompose(modifier: Modifier=Modifier){
    Box (
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        ProgressBar.CircleProgressBar()

    }

}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun WordsDetectorListCompose(viewModel: SmsAnalysisViewModel, list: List<WordDetectorEntity>){
    val deleteAction = Action<WordDetectorEntity>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_delete ), color = Color.White,alignment = TextAlign.Center) },
        { ActionIcon(id = R.drawable.ic_delete ) },
        backgroundColor = MusrofatyTheme.colors.deleteActionColor,
        onClicked = { position, item ->
            viewModel.deleteWordDetector(item.id)

        })

    VerticalEasyList(
        list            = list,
        view            = { TextComponent.BodyText(modifier = Modifier.fillMaxWidth().padding(dimensionResource(id = R.dimen.default_margin16)), text = it.word) },
        dividerView     = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked   = { item, position ->

        },
        endActions      = listOf(deleteAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView       = { EmptyCompose() },
    )
}

@Composable
fun EmptyCompose(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
    }

}