package com.msharialsayari.musrofaty.ui.toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.msharialsayari.musrofaty.ui.theme.MusrofatyComposeTheme
import com.msharialsayari.musrofaty.ui_component.TextComponent
import kotlin.math.roundToInt


private val ContentPadding = 8.dp
private val Elevation = 4.dp

private const val Alpha = 0.75f

private val ExpandedPadding = 1.dp
private val CollapsedPadding = 3.dp

private val ExpandedCostaRicaHeight = 20.dp
private val CollapsedCostaRicaHeight = 16.dp

private val ExpandedWildlifeHeight = 32.dp
private val CollapsedWildlifeHeight = 24.dp

private val MapHeight = CollapsedCostaRicaHeight * 2

@Preview
@Composable
fun CollapsingToolbarCollapsedPreview() {
    MusrofatyComposeTheme {
        CollapsingToolbar(
            title = "Sender",
            progress = 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )
    }
}

@Preview
@Composable
fun CollapsingToolbarHalfwayPreview() {
    MusrofatyComposeTheme {
        CollapsingToolbar(
            title = "Sender",
            progress = 0.5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
    }
}

@Preview
@Composable
fun CollapsingToolbarExpandedPreview() {
    MusrofatyComposeTheme {
        CollapsingToolbar(
            title = "Sender",
            progress = 1f,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )
    }
}


@Composable
fun CollapsingToolbar(
    modifier   : Modifier = Modifier,
    title      :String,
    background : (@Composable () -> Unit)?=null,
    progress   : Float,

) {
    val costaRicaHeight = with(LocalDensity.current) {
        lerp(CollapsedCostaRicaHeight.toPx(), ExpandedCostaRicaHeight.toPx(), progress).toDp()
    }
    val wildlifeHeight = with(LocalDensity.current) {
        lerp(CollapsedWildlifeHeight.toPx(), ExpandedWildlifeHeight.toPx(), progress).toDp()
    }
    val logoPadding = with(LocalDensity.current) {
        lerp(CollapsedPadding.toPx(), ExpandedPadding.toPx(), progress).toDp()
    }

    Surface(
        elevation = Elevation,
        modifier = modifier
    ) {
        Box (modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier.fillMaxSize()
                .align(BiasAlignment(0f, 1f - ((1f - progress) * 0.75f)))
                .graphicsLayer { alpha = progress * Alpha }
            ) {

                background?.invoke()

            }




            //#endregion
            Box(modifier = Modifier.fillMaxSize()
                .graphicsLayer { alpha = ((0.25f - progress) * 4).coerceIn(0f, 1f) },
            ) {

                    TextComponent.HeaderText( modifier = Modifier.padding(top = 40.dp, start = 20.dp, end = 20.dp),text = title)


            }
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