package com.msharialsayari.musrofaty.ui_component

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.common.net.MediaType

@Composable
fun singleMediaPicker(
    onSelectMedia: (Uri) -> Unit
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {

   return  rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onSelectMedia(uri)
            }

        })



}