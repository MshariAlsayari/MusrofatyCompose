package com.msharialsayari.musrofaty.utils.enums

enum class ScreenType(val isBottomNavigation: Boolean, val isScreenWithDetails: Boolean) {
    Compact(true, false),
    Medium(false, true),
    Expanded(false, true);

    fun isPortrait(): Boolean {
        return this == Compact || this == Medium
    }

    fun isLandscape(): Boolean {
        return this == Expanded
    }
}