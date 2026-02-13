package com.example.habit_tracker_kotlin.model

enum class HabitType { CHECK, COUNTER }

data class Habit(
    val id: String = "",
    val name: String,
    val iconId: String,
    val type: HabitType,
    val streak: Int = 0,
    val doneToday: Boolean = false,
    val countToday: Int = 0,
    val targetPerDay: Int = 1,
    val heatmapColorArgb: Int = 0xFF0D47A1.toInt()
)
