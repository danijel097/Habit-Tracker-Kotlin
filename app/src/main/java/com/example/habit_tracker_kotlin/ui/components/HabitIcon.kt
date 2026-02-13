package com.example.habit_tracker_kotlin.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

private fun habitIcon(iconId: String): ImageVector {
    return when (iconId) {
        "water" -> Icons.Filled.WaterDrop
        "gym" -> Icons.Filled.FitnessCenter
        "book" -> Icons.Filled.MenuBook
        "sleep" -> Icons.Filled.Bedtime
        "study" -> Icons.Filled.School
        "run" -> Icons.Filled.DirectionsRun
        "meditation" -> Icons.Filled.SelfImprovement
        "food" -> Icons.Filled.Restaurant
        "coffee" -> Icons.Filled.LocalCafe
        "music" -> Icons.Filled.MusicNote
        "work" -> Icons.Filled.Work
        "home" -> Icons.Filled.Home
        else -> Icons.Filled.CheckCircle
    }
}

@Composable
fun HabitIcon(iconId: String, modifier: Modifier = Modifier, contentDescription: String? = null) {
    Icon(
        imageVector = habitIcon(iconId),
        contentDescription = contentDescription,
        modifier = modifier
    )
}