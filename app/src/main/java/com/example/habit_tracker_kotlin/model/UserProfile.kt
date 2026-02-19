package com.example.habit_tracker_kotlin.model

data class UserProfile(
    val uid: String = "",
    val firstName: String,
    val lastName: String,
    val email: String,
    val isAdmin: Boolean = false
)
