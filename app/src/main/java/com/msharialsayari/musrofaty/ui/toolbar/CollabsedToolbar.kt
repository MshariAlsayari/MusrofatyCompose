package com.msharialsayari.musrofaty.ui.toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


private val Elevation = 0.dp


@Composable
fun CollapsingToolbar(
    modifier            : Modifier = Modifier,
    actions             : @Composable (() -> Unit)? = null,
    collapsedComposable : @Composable (() -> Unit)? = null,
    expandedComposable  : @Composable (() -> Unit)? = null,
    progress            : Float,


    ) {

    Surface(
        elevation = Elevation,
        modifier = modifier
    ) {
        Box (modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                actions?.invoke()
                Box(modifier = Modifier.fillMaxSize()) {
                    ExpandedToolbarCompose( content = expandedComposable, progress = progress)
                    CollapsedToolbarCompose( content = collapsedComposable, progress = progress)
                }


            }

        }
    }
}

@Composable
private fun ExpandedToolbarCompose(
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
    progress            : Float,
){


    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = progress }
    ) {
        content?.invoke()

    }



}

@Composable
private fun CollapsedToolbarCompose(
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
    progress            : Float,
){


    Box(modifier = modifier
        .padding(horizontal = 16.dp )
        .fillMaxSize()
        .graphicsLayer { alpha = ((0.25f - progress) * 4).coerceIn(0f, 1f) },
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            content?.invoke()

        }

    }



}

