package com.msharialsayari.musrofaty.ui.permission

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun singlePermission(permission:String):PermissionStatus {
    val permissionState = rememberPermissionState(permission = permission)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchPermissionRequest()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    when {
        permissionState.hasPermission -> {
            return PermissionStatus.GRANTED
        }
        permissionState.shouldShowRationale -> {
            //Text(text = "Reading external permission is required by this app")
            return PermissionStatus.SHOULD_SHOW_DIALOG
        }
        !permissionState.hasPermission && !permissionState.shouldShowRationale -> {
         //   Text(text = "Permission fully denied. Go to settings to enable")
            return PermissionStatus.SHOULD_SHOW_DIALOG

        }

        else->{
            return PermissionStatus.SHOULD_SHOW_DIALOG
        }
    }
}