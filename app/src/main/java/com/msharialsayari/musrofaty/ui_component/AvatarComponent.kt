package com.msharialsayari.musrofaty.ui_component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msharialsayari.musrofaty.R

@Composable
fun AvatarComponent(icon:String, onClicked:()->Unit = {}) {

    val defaultIcon = R.drawable.ic_app
    val iconSize= 50
    val modifier = Modifier
        .width(iconSize.dp)
        .height(iconSize.dp)
        .clip(CircleShape)
        .clickable {
            onClicked()
        }

    if(icon.isEmpty()){
        Image(
            modifier = modifier,
            painter = painterResource(id = defaultIcon), contentDescription = null)
    }else{
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .size(iconSize)
                .data(Uri.parse(icon))
                .error(defaultIcon)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = defaultIcon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }

}