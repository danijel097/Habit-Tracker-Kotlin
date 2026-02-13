package com.example.habit_tracker_kotlin.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.model.HabitStore
import com.example.habit_tracker_kotlin.model.HabitType
import com.example.habit_tracker_kotlin.ui.components.HabitCheckCard
import com.example.habit_tracker_kotlin.ui.components.HabitCounterCard

@Composable
fun HomeScreen(
    onAddHabit: () -> Unit = {},
    onHabitClick: (String) -> Unit
) {
    val habits = HabitStore.habits

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Habits", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(habits, key = { it.id }) { habit ->
                    when (habit.type) {
                        HabitType.CHECK -> HabitCheckCard(
                            habit = habit,
                            onToggleDone = { HabitStore.toggleDone(habit.id) },
                            onClick = { onHabitClick(habit.id) }
                        )

                        HabitType.COUNTER -> HabitCounterCard(
                            habit = habit,
                            onMinus = { HabitStore.decCounter(habit.id) },
                            onPlus = { HabitStore.incCounter(habit.id) },
                            onClick = { onHabitClick(habit.id) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddHabit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add habit")
        }
    }
}
