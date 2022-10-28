package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R

@Composable
fun AvatarComponent(icon:Int?) {
    Image(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .clip(CircleShape),
        painter = painterResource(id = icon?: R.drawable.ic_app), contentDescription = null)
}