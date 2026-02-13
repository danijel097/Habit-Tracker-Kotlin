package com.example.habit_tracker_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.habit_tracker_kotlin.layout.MainLayout
import com.example.habit_tracker_kotlin.ui.theme.Habit_Tracker_KotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Habit_Tracker_KotlinTheme(darkTheme = AppSettings.darkMode.value) {
                MainLayout()
            }
        }
    }
}
