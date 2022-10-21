package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.utils.mirror

object AppBarComponent {

    @Composable
    fun TopBarComponent( @StringRes title:Int?, onArrowBackClicked:()->Unit={}, actions: @Composable RowScope.() -> Unit = {}, isParent:Boolean = false){
        val navigationIcon: @Composable () -> Unit = {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier= Modifier
                        .mirror()
                        .clickable { onArrowBackClicked() },
                )
            }



        val appbarTitle = if (title != null) stringResource(id = title) else ""
        AnimatedVisibility(
            visible = title != null,
        ) {

            TopAppBar(
                title = { Text(appbarTitle) },
                navigationIcon = if (isParent) null else navigationIcon,
                actions = actions,
                contentColor = Color.White
            )
        }

    }
}