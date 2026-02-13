package com.example.habit_tracker_kotlin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.model.Habit

@Composable
fun HabitCheckCard(
    habit: Habit,
    onToggleDone: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HabitIcon(habit.iconId)
                Spacer(Modifier.width(12.dp))

                Column {
                    Text(habit.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Streak: ${habit.streak} days",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // DONE / UNDO
            if (habit.doneToday) {
                Button(onClick = onToggleDone) { Text("UNDO") }
            } else {
                OutlinedButton(onClick = onToggleDone) { Text("DONE") }
            }
        }
    }
}
