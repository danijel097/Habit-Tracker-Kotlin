package com.example.habit_tracker_kotlin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppRoute(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Login : AppRoute("login", "Login", Icons.Filled.AccountCircle)
    data object Register : AppRoute("register", "Register", Icons.Filled.AccountCircle)

    data object Home : AppRoute("home", "Home", Icons.Filled.Home)
    data object History : AppRoute("history", "History", Icons.Filled.History)
    data object Account : AppRoute("account", "Account", Icons.Filled.AccountCircle)

    data object HabitDetails : AppRoute("habit/{habitId}", "Habit", Icons.Filled.AccountCircle) {
        fun createRoute(habitId: String) = "habit/$habitId"
    }
    data object EditHabit : AppRoute("edit_habit/{habitId}", "Edit Habit", Icons.Filled.AccountCircle) {
        fun createRoute(habitId: String) = "edit_habit/$habitId"
    }

    data object CreateHabit : AppRoute("create_habit", "Create Habit", Icons.Filled.AccountCircle)
    data object EditAccount : AppRoute("edit_account", "Edit Account", Icons.Filled.AccountCircle)
}

val bottomNavItems = listOf(
    AppRoute.Home,
    AppRoute.History,
    AppRoute.Account
)