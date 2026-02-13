package com.example.habit_tracker_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.viewmodel.HabitStore
import com.example.habit_tracker_kotlin.ui.components.HabitHistoryCard

@Composable
fun HistoryScreen(onHabitClick: (String) -> Unit) {

    val habits = HabitStore.habits

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("History", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(habits, key = { it.id }) { habit ->
                HabitHistoryCard(
                    habit = habit,
                    onClick = { onHabitClick(habit.id) },
                    valueForDate = { date ->
                        HabitStore.ratioForDate(habit.id, date, habit.type, habit.targetPerDay)
                    }
                )
            }
        }
    }
}
