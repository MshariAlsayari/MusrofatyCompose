package com.msharialsayari.musrofaty.ui.toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


private val ContentPadding = 8.dp
private val Elevation = 2.dp

private const val Alpha = 0.75f

private val ExpandedPadding = 1.dp
private val CollapsedPadding = 3.dp

private val ExpandedCostaRicaHeight = 20.dp
private val CollapsedCostaRicaHeight = 16.dp

private val ExpandedWildlifeHeight = 32.dp
private val CollapsedWildlifeHeight = 24.dp

private val MapHeight = CollapsedCostaRicaHeight * 2

//@Preview
//@Composable
//fun CollapsingToolbarCollapsedPreview() {
//    MusrofatyComposeTheme {
//        CollapsingToolbar(
//            title = "Sender",
//            progress = 0f,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(80.dp)
//        )
//    }
//}
//
//@Preview
//@Composable
//fun CollapsingToolbarHalfwayPreview() {
//    MusrofatyComposeTheme {
//        CollapsingToolbar(
//            title = "Sender",
//            progress = 0.5f,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(120.dp)
//        )
//    }
//}
//
//@Preview
//@Composable
//fun CollapsingToolbarExpandedPreview() {
//    MusrofatyComposeTheme {
//        CollapsingToolbar(
//            title = "Sender",
//            progress = 1f,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(160.dp)
//        )
//    }
//}


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




@Composable
private fun CollapsingToolbarLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->


        val placeables = measurables.map {
            it.measure(constraints)
        }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {

            val expandedHorizontalGuideline = (constraints.maxHeight * 0.4f).roundToInt()
            val collapsedHorizontalGuideline = (constraints.maxHeight * 0.5f).roundToInt()

            val countryMap = placeables[0]

            countryMap.placeRelative(
                x = 0,
                y = collapsedHorizontalGuideline - countryMap.height / 2,
            )

        }
    }
}