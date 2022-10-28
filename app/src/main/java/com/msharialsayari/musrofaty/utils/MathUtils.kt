package com.msharialsayari.musrofaty.utils

object MathUtils {

    fun calculatePercentage(part: Double, total: Double): Double {

        return try {
            if (total != 0.0)
                part * 100.0 / total
            else
                0.0
        } catch (e: Exception) {
            0.0

        }

    }
}