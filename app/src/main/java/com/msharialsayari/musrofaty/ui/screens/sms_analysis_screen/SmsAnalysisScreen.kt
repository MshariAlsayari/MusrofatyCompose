package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SmsAnalysisScreen(){
    val viewModel:SmsAnalysisViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    Box {
        Text(text = "Sms Analysis")

    }
}