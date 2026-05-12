package com.nammaraste.health.utils

import androidx.compose.ui.graphics.Color
import com.nammaraste.health.ui.theme.DamageRed
import com.nammaraste.health.ui.theme.RoadGreen

object HealthScoreCalculator {
    fun getHealthLabel(score: Int): String = when {
        score >= 80 -> "Excellent"
        score >= 60 -> "Good"
        score >= 40 -> "Moderate"
        score >= 20 -> "Poor"
        else -> "Critical"
    }

    fun getHealthColor(score: Int): Color = when {
        score >= 60 -> RoadGreen
        score >= 40 -> Color(0xFFFFA000)  // Amber
        else -> DamageRed
    }
}
