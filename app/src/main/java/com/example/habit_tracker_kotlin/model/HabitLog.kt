package com.example.habit_tracker_kotlin.model

data class HabitLog(
    val habitId: String,
    val date: String,
    val type: HabitType,
    val done: Boolean = false,
    val count: Int = 0,
    val target: Int = 1
)
