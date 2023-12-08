package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.utils.enums.ScreenType
import com.msharialsayari.musrofaty.utils.mirror

@Composable
fun SenderListTopBar(onAddSenderClicked: ()->Unit){

    val screenType = MusrofatyTheme.screenType

    AppBarComponent.TopBarComponent(
        title = BottomNavItem.SendersList.title,
        isParent = true,
        actions = {

            if (screenType.isScreenWithDetails)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .mirror()
                            .clickable {
                                onAddSenderClicked()
                            })

                }

        }
    )

}