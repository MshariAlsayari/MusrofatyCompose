package com.msharialsayari.musrofaty.ui.screens.sms_detector_tool_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.sms_types_screen.SmsTypesContent
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldComponent
import com.msharialsayari.musrofaty.utils.enums.SmsType

@Composable
fun SmsDetectorToolScreen(navigatorViewModel: AppNavigatorViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SmsDetectorToolScreen.title,
                onArrowBackClicked = { navigatorViewModel.navigateUp() }
            )

        },
    ) { innerPadding ->
        SmsDetectorToolContent(modifier = Modifier.fillMaxSize().padding(innerPadding))
    }

}

@Composable
fun SmsDetectorToolContent(modifier: Modifier = Modifier) {

    val viewModel: SmsDetectorToolViewModel = hiltViewModel()
    Column(
        modifier = modifier,
    ) {
        SmsTextField(viewModel =viewModel)
        SmsInfo(viewModel = viewModel)
    }

}

@Composable
private fun SmsTextField(
    viewModel: SmsDetectorToolViewModel
) {

    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val textFieldHeight = screenHeight * 0.3

    var textValue by remember { mutableStateOf("") }
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .height(textFieldHeight.dp)
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = textValue,
        label = R.string.sms_title_screen,
        onValueChanged = {
            textValue = it
            viewModel.onSmsTextFieldChanged(it)
        }
    )
}


@Composable
private fun SmsInfo(
    modifier: Modifier = Modifier,
    viewModel: SmsDetectorToolViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val smsType = uiState.smsType
    val currency = uiState.currency
    val amount = uiState.amount
    val storeName = uiState.storeName

    val storeNameTitle = when(smsType){
        SmsType.OUTGOING_TRANSFER -> stringResource(id = R.string.common_receiver)
        SmsType.PAY_BILLS -> stringResource(id = R.string.bill)
        else -> stringResource(id = R.string.store)
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        SmsInfoRow(title = stringResource(id = R.string.pref_managment_sms_types_title), value = stringResource(id = smsType.valueString))
        SmsInfoRow(title = storeNameTitle, value = storeName)
        SmsInfoRow(title = stringResource(id = R.string.common_amount), value = amount )
        SmsInfoRow(title = stringResource(id = R.string.tab_currency), value = currency)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SmsInfoRow(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {

    ListItem(
        modifier = modifier,
        text = { Text(text = title) },
        trailing = { TextComponent.PlaceholderText(text = value) }
    )
}
