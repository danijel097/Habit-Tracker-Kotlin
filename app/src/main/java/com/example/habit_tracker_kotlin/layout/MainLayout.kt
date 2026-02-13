package com.example.habit_tracker_kotlin.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.habit_tracker_kotlin.viewmodel.AuthStore
import com.example.habit_tracker_kotlin.navigation.AppRoute
import com.example.habit_tracker_kotlin.navigation.bottomNavItems
import com.example.habit_tracker_kotlin.ui.screens.*

@Composable
fun MainLayout() {
    val navController = rememberNavController()
    val isAuthenticated = AuthStore.isAuthenticated.value

    if (!isAuthenticated) {
        NavHost(
            navController = navController,
            startDestination = AppRoute.Login.route
        ) {
            composable(AppRoute.Login.route) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(AppRoute.Register.route)
                    }
                )
            }

            composable(AppRoute.Register.route) {
                RegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    } else {
        Scaffold(
            bottomBar = { BottomNavBar(navController = navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppRoute.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppRoute.Home.route) {
                    HomeScreen(
                        onAddHabit = { navController.navigate(AppRoute.CreateHabit.route) },
                        onHabitClick = { habitId ->
                            navController.navigate(AppRoute.HabitDetails.createRoute(habitId))
                        }
                    )
                }

                composable(AppRoute.CreateHabit.route) {
                    CreateHabitScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(AppRoute.History.route) {
                    HistoryScreen { habitId ->
                        navController.navigate(AppRoute.HabitDetails.createRoute(habitId))
                    }
                }

                composable(AppRoute.Account.route) {
                    AccountScreen(
                        onEdit = { navController.navigate(AppRoute.EditAccount.route) }
                    )
                }

                composable(AppRoute.EditAccount.route) {
                    EditAccountScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = AppRoute.HabitDetails.route,
                    arguments = listOf(navArgument("habitId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val habitId = backStackEntry.arguments?.getString("habitId") ?: ""

                    HabitDetailsScreen(
                        habitId = habitId,
                        onBack = { navController.popBackStack() },
                        onEdit = { id -> navController.navigate(AppRoute.EditHabit.createRoute(id)) }
                    )
                }

                composable(
                    route = AppRoute.EditHabit.route,
                    arguments = listOf(navArgument("habitId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val habitId = backStackEntry.arguments?.getString("habitId") ?: ""

                    EditHabitScreen(
                        habitId = habitId,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination

        val isOnDetails = currentDestination?.hierarchy?.any {
            it.route == AppRoute.HabitDetails.route || it.route == AppRoute.EditHabit.route || it.route == AppRoute.CreateHabit.route
        } == true

        bottomNavItems.forEach { item ->
            val selected = !isOnDetails && currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (isOnDetails) {
                        navController.popBackStack()
                    }

                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
