package com.msharialsayari.musrofaty.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color


val Black          = Color(0xFF000000)
val White          = Color(0xFFFFFFFF)
val Red            = Color(0xFFCC0311)
val BlackOnyx      = Color(0xFF1D252D)
val Air            = Color(0xFFf3f3f1)
val LightGray      = Color(0xFF979797)
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
private val secondaryVariantDark = secondaryDark
private val backgroundDark       = BlackOnyx
private val surfaceDark          = backgroundDark
private val errorDark            = error
private val onPrimaryDark        = onPrimary
private val onBackgroundDark     = White
private val onSecondaryDark      = onBackgroundDark
private val onSurfaceDark        = onBackgroundDark
private val onErrorDark          = onError

//Musrofaty Light Colors
private val incomeColor          = Color(0xFF01852D)
private val expensesColor        = Color(0xFFCC0311)
private val deleteActionColor    = Color(0xFFF44336)
private val pinActionColor       = Color(0xFF4CAF50)
private val modifyActionColor    = Color(0xFF2196F3)
private val toolbarColor         = background
private val textHeaderColor      = onBackground
private val textBodyColor        = onBackground
private val textPlaceHolderColor = LightGray
private val textClickableColor   = primary
private val iconBackgroundColor  = LightGray
private val activeColor          = primary
private val selectedItemColor    = primary
private val navigationBarColor   = background
private val bottomNavigationBarColor   = background
private val statusBarColor       = toolbarColor

//Musrofaty Dark Colors
private val toolbarDarkColor         = backgroundDark
private val textHeaderDarkColor      = Air
private val textBodyDarkColor        = textHeaderDarkColor
private val textClickableDarkColor   = secondaryDark
private val selectedItemDarkColor    = onBackgroundDark
private val iconBackgroundDarkColor  = LightGray
private val activeDarkColor          = textClickableDarkColor
private val navigationBarDarkColor   = backgroundDark
private val bottomNavigationBarDarkColor   = backgroundDark
private val statusBarDarkColor       = toolbarDarkColor

val LightColors = MusrofatyColors(
    primary               = primary,
    primaryVariant        = primaryVariant,
    secondary             = secondary,
    secondaryVariant      = secondaryVariant,
    background            = background,
    surface               = surface,
    error                 = error,
    onPrimary             = onPrimary,
    onSecondary           = onSecondary,
    onBackground          = onBackground,
    onSurface             = onSurface,
    onError               = onError,
    onBackgroundIconColor = LightGray,
    incomeColor           = incomeColor,
    expensesColor         = expensesColor,
    deleteActionColor     = deleteActionColor,
    pinActionColor        = pinActionColor,
    modifyActionColor     = modifyActionColor,
    toolbarColor          = toolbarColor,
    textHeaderColor       = textHeaderColor,
    textBodyColor         = textBodyColor,
    textPlaceHolderColor  = textPlaceHolderColor,
    textClickableColor    = textClickableColor,
    iconBackgroundColor   = iconBackgroundColor,
    activeColor           = activeColor,
    selectedItemColor     = selectedItemColor,
    navigationBarColor    = navigationBarColor,
    statusBarColor        = statusBarColor,
    bottomNavigationBarColor = bottomNavigationBarColor,
    isLight               = true
)

val DarkColors = MusrofatyColors(
    primary               = primaryDark,
    primaryVariant        = primaryVariantDark,
    secondary             = secondaryDark,
    secondaryVariant      = secondaryVariantDark,
    background            = backgroundDark,
    surface               = surfaceDark,
    error                 = errorDark,
    onPrimary             = onPrimaryDark,
    onSecondary           = onSecondaryDark,
    onBackground          = onBackgroundDark,
    onSurface             = onSurfaceDark,
    onError               = onErrorDark,
    onBackgroundIconColor = White ,
    incomeColor           = incomeColor,
    expensesColor         = expensesColor,
    deleteActionColor     = deleteActionColor,
    pinActionColor        = pinActionColor,
    modifyActionColor     = modifyActionColor,
    toolbarColor          = toolbarDarkColor,
    textHeaderColor       = textHeaderDarkColor,
    textBodyColor         = textBodyDarkColor,
    textPlaceHolderColor  = textPlaceHolderColor,
    textClickableColor    = textClickableDarkColor,
    iconBackgroundColor   = iconBackgroundDarkColor,
    activeColor           = activeDarkColor,
    selectedItemColor     = selectedItemDarkColor,
    navigationBarColor    = navigationBarDarkColor,
    statusBarColor        = statusBarDarkColor,
    bottomNavigationBarColor = bottomNavigationBarDarkColor,
    isLight               = false
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
    onBackgroundIconColor: Color = Color.Unspecified,
    incomeColor: Color = Color.Unspecified,
    expensesColor: Color = Color.Unspecified,
    deleteActionColor: Color = Color.Unspecified,
    pinActionColor: Color = Color.Unspecified,
    modifyActionColor: Color = Color.Unspecified,
    toolbarColor: Color = Color.Unspecified,
    textHeaderColor: Color = Color.Unspecified,
    textBodyColor: Color = Color.Unspecified,
    textPlaceHolderColor: Color = Color.Unspecified,
    textClickableColor: Color = Color.Unspecified,
    iconBackgroundColor: Color = Color.Unspecified,
    activeColor: Color = Color.Unspecified,
    selectedItemColor: Color = Color.Unspecified,
    navigationBarColor: Color = Color.Unspecified,
    statusBarColor: Color = Color.Unspecified,
    bottomNavigationBarColor: Color = Color.Unspecified,
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

    var onBackgroundIconColor by mutableStateOf(onBackgroundIconColor, structuralEqualityPolicy())
        internal set

    var incomeColor by mutableStateOf(incomeColor, structuralEqualityPolicy())
        internal set

    var expensesColor by mutableStateOf(expensesColor, structuralEqualityPolicy())
        internal set

    var deleteActionColor by mutableStateOf(deleteActionColor, structuralEqualityPolicy())
        internal set

    var pinActionColor by mutableStateOf(pinActionColor, structuralEqualityPolicy())
        internal set

    var modifyActionColor by mutableStateOf(modifyActionColor, structuralEqualityPolicy())
        internal set

    var toolbarColor by mutableStateOf(toolbarColor, structuralEqualityPolicy())
        internal set


    var textHeaderColor by mutableStateOf(textHeaderColor, structuralEqualityPolicy())
        internal set

    var textBodyColor by mutableStateOf(textBodyColor, structuralEqualityPolicy())
        internal set

    var textPlaceHolderColor by mutableStateOf(textPlaceHolderColor, structuralEqualityPolicy())
        internal set

    var textClickableColor by mutableStateOf(textClickableColor, structuralEqualityPolicy())
        internal set

    var iconBackgroundColor by mutableStateOf(iconBackgroundColor, structuralEqualityPolicy())
        internal set

    var activeColor by mutableStateOf(activeColor, structuralEqualityPolicy())
        internal set

    var selectedItemColor by mutableStateOf(selectedItemColor, structuralEqualityPolicy())
        internal set

    var navigationBarColor by mutableStateOf(navigationBarColor, structuralEqualityPolicy())
        internal set

    var statusBarColor by mutableStateOf(statusBarColor, structuralEqualityPolicy())
        internal set

    var bottomNavigationBarColor by mutableStateOf(bottomNavigationBarColor, structuralEqualityPolicy())
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
        onBackgroundIconColor = other.onBackgroundIconColor
        toolbarColor = other.toolbarColor
        activeColor = other.activeColor
        incomeColor= other.incomeColor
        expensesColor= other.expensesColor
        deleteActionColor= other.deleteActionColor
        pinActionColor= other.pinActionColor
        modifyActionColor= other.modifyActionColor
        textHeaderColor= other.textHeaderColor
        textBodyColor= other.textBodyColor
        textPlaceHolderColor= other.textPlaceHolderColor
        textClickableColor= other.textClickableColor
        iconBackgroundColor= other.iconBackgroundColor
        selectedItemColor= other.selectedItemColor
        navigationBarColor= other.navigationBarColor
        statusBarColor = other.statusBarColor
        bottomNavigationBarColor = other.bottomNavigationBarColor
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
        onBackgroundIconColor: Color = this.onBackgroundIconColor,
        activeColor : Color = this.activeColor,
        toolbarColor: Color = this.toolbarColor,
        incomeColor: Color = this.incomeColor,
        expensesColor: Color = this.expensesColor,
        deleteActionColor: Color = this.deleteActionColor,
        pinActionColor: Color = this.pinActionColor,
        modifyActionColor: Color = this.modifyActionColor,
        textHeaderColor: Color = this.textHeaderColor,
        textBodyColor: Color = this.textBodyColor,
        textPlaceHolderColor: Color = this.textPlaceHolderColor,
        textClickableColor: Color = this.textClickableColor,
        iconBackgroundColor: Color = this.iconBackgroundColor,
        selectedItemColor: Color = this.selectedItemColor,
        navigationBarColor: Color = this.navigationBarColor,
        statusBarColor: Color = this.statusBarColor,
        bottomNavigationBarColor:Color = this.bottomNavigationBarColor,
        isLight: Boolean = this.isLight,

    ): MusrofatyColors = MusrofatyColors(
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
        onBackgroundIconColor = onBackgroundIconColor,
        toolbarColor = toolbarColor,
        activeColor = activeColor,
        incomeColor = incomeColor,
        expensesColor = expensesColor,
        deleteActionColor = deleteActionColor,
        pinActionColor = pinActionColor,
        modifyActionColor = modifyActionColor,
        textHeaderColor = textHeaderColor,
        textBodyColor = textBodyColor,
        textPlaceHolderColor = textPlaceHolderColor,
        textClickableColor = textClickableColor,
        iconBackgroundColor = iconBackgroundColor,
        selectedItemColor = selectedItemColor,
        navigationBarColor = navigationBarColor,
        statusBarColor = statusBarColor,
        bottomNavigationBarColor = bottomNavigationBarColor,
        isLight = isLight
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