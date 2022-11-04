package com.msharialsayari.musrofaty.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val Red = Color(0xFFCC0311)
val BlackOnyx = Color(0xFF1D252D)
val Air = Color(0xFFf3f3f1)
val LightGray = Color(0xFF979797)
val LightBlackOnyx = Color(0xFF333A42)





//Primitive Light Colors
private val primary          = Color(0xFF006039)
private val primaryVariant   = primary
private val secondary        = primary
private val secondaryVariant = primary
private val background       = White
private val surface          = background
private val error            = Red
private val onPrimary        = White
private val onSecondary      = White
private val onBackground     = Black
private val onSurface        = onBackground
private val onError          = White

//Primitive Dark Colors
private val primaryDark          = primary
private val primaryVariantDark   = primary
private val secondaryDark        = Color(0xFFA37E2C)
private val secondaryVariantDark = Color(0xFFA37E2C)
private val backgroundDark       = BlackOnyx
private val surfaceDark          = backgroundDark
private val errorDark            = error
private val onPrimaryDark        = onPrimary
private val onSecondaryDark      = White
private val onBackgroundDark     = White
private val onSurfaceDark        = onBackgroundDark
private val onErrorDark          = onError




val LightColors = MusrofatyColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError,
    toolbarColor = primary ,
    onBackgroundIconColor = LightGray,
    activeColor = primary,
    isLight = true
)

val DarkColors = MusrofatyColors(
    primary = primaryDark,
    primaryVariant = primaryVariantDark,
    secondary = secondaryDark,
    secondaryVariant = secondaryVariantDark,
    background = backgroundDark,
    surface = surfaceDark,
    error = errorDark,
    onPrimary = onPrimaryDark,
    onSecondary = onSecondaryDark,
    onBackground = onBackgroundDark,
    onSurface = onSurfaceDark,
    onError = onErrorDark,
    toolbarColor = LightBlackOnyx,
    onBackgroundIconColor = White ,
    activeColor = secondaryDark,
    isLight = false
)



@Stable
class MusrofatyColors(
    primary: Color = Color.Unspecified,
    primaryVariant: Color = Color.Unspecified,
    secondary: Color = Color.Unspecified,
    secondaryVariant: Color = Color.Unspecified,
    background: Color = Color.Unspecified,
    surface: Color = Color.Unspecified,
    error: Color = Color.Unspecified,
    onPrimary: Color = Color.Unspecified,
    onSecondary: Color = Color.Unspecified,
    onBackground: Color = Color.Unspecified,
    onSurface: Color = Color.Unspecified,
    onError: Color = Color.Unspecified,
    toolbarColor: Color = Color.Unspecified,
    onBackgroundIconColor: Color = Color.Unspecified,
    activeColor: Color = Color.Unspecified,
    isLight: Boolean = true
) {
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var primaryVariant by mutableStateOf(primaryVariant, structuralEqualityPolicy())
        internal set
    var secondary by mutableStateOf(secondary, structuralEqualityPolicy())
        internal set
    var secondaryVariant by mutableStateOf(secondaryVariant, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var surface by mutableStateOf(surface, structuralEqualityPolicy())
        internal set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        internal set
    var onPrimary by mutableStateOf(onPrimary, structuralEqualityPolicy())
        internal set
    var onSecondary by mutableStateOf(onSecondary, structuralEqualityPolicy())
        internal set
    var onBackground by mutableStateOf(onBackground, structuralEqualityPolicy())
        internal set
    var onSurface by mutableStateOf(onSurface, structuralEqualityPolicy())
        internal set
    var onError by mutableStateOf(onError, structuralEqualityPolicy())
        internal set


    var toolbarColor by mutableStateOf(toolbarColor, structuralEqualityPolicy())
        internal set
    var onBackgroundIconColor by mutableStateOf(onBackgroundIconColor, structuralEqualityPolicy())
        internal set
    var activeColor by mutableStateOf(activeColor, structuralEqualityPolicy())
        internal set


    var isLight by mutableStateOf(isLight, structuralEqualityPolicy())
        internal set

    fun updateColorsFrom(other: MusrofatyColors) {
        primary = other.primary
        primaryVariant = other.primaryVariant
        secondary = other.secondary
        secondaryVariant = other.secondaryVariant
        background = other.background
        surface = other.surface
        error = other.error
        onPrimary = other.onPrimary
        onSecondary = other.onSecondary
        onBackground = other.onBackground
        onSurface = other.onSurface
        onError = other.onError
        toolbarColor = other.toolbarColor
        onBackgroundIconColor = other.onBackgroundIconColor
        activeColor = other.activeColor
        isLight = other.isLight

    }

    fun copy(
        primary: Color = this.primary,
        primaryVariant: Color = this.primaryVariant,
        secondary: Color = this.secondary,
        secondaryVariant: Color = this.secondaryVariant,
        background: Color = this.background,
        surface: Color = this.surface,
        error: Color = this.error,
        onPrimary: Color = this.onPrimary,
        onSecondary: Color = this.onSecondary,
        onBackground: Color = this.onBackground,
        onSurface: Color = this.onSurface,
        onError: Color = this.onError,
        toolbarColor: Color = this.toolbarColor,
        onBackgroundIconColor: Color = this.onBackgroundIconColor,
        activeColor : Color = this.activeColor,
        isLight: Boolean = this.isLight,

    ): MusrofatyColors = MusrofatyColors(
        primary,
        primaryVariant,
        secondary,
        secondaryVariant,
        background,
        surface,
        error,
        onPrimary,
        onSecondary,
        onBackground,
        onSurface,
        onError,
        toolbarColor,
        onBackgroundIconColor,
        activeColor,
        isLight
    )
}



fun mapBasicColors(
    colors: MusrofatyColors,
    darkTheme: Boolean,
) = Colors(
    primary = colors.primary,
    primaryVariant = colors.primaryVariant,
    secondary = colors.secondary,
    secondaryVariant = colors.secondaryVariant,
    background = colors.background,
    surface = colors.surface,
    error = colors.error,
    onPrimary = colors.onPrimary,
    onSecondary = colors.onSecondary,
    onBackground = colors.onBackground,
    onSurface = colors.onSurface,
    onError = colors.onError,
    isLight = !darkTheme
)