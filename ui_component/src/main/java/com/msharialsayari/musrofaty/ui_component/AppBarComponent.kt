package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.utils.mirror

object AppBarComponent {

    @Composable
    fun TopBarComponent( title:String="", topBarState: MutableState<Boolean>,
                        onArrowBackClicked:()->Unit={}, isParent:Boolean = true){
        val navigationIcon: @Composable () -> Unit = {
                Icon(
                    modifier=Modifier.mirror().clickable { onArrowBackClicked() },
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }

        AnimatedVisibility(
            visible = topBarState.value,
        ) {

            TopAppBar(
                title = { Text(title) },
                navigationIcon = if (isParent) null else navigationIcon,
                contentColor = Color.White
            )
        }
    }
}