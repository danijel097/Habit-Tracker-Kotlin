package com.example.habit_tracker_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.viewmodel.HabitStore
import com.example.habit_tracker_kotlin.model.HabitType
import com.example.habit_tracker_kotlin.ui.components.HeatmapMode
import com.example.habit_tracker_kotlin.ui.components.HabitHeatmap
import com.example.habit_tracker_kotlin.ui.components.HabitIcon

@Composable
fun HabitDetailsScreen(
    habitId: String,
    onBack: () -> Unit,
    onEdit: (String) -> Unit
) {
    val habit = HabitStore.getById(habitId)

    if (habit == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Habit not found (id=$habitId)")
        }
        return
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete habit") },
            text = { Text("Are you sure you want to delete \"${habit.name}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        HabitStore.deleteHabit(habit.id)
                        showDeleteDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(12.dp))
            Text("Habit Details", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { onEdit(habit.id) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit habit")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete habit",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        // Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HabitIcon(
                    iconId = habit.iconId,
                    modifier = Modifier.size(56.dp),
                    contentDescription = habit.name
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (habit.type == HabitType.CHECK)
                            "Once per day" else "Multiple times per day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Heatmap Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Activity Heatmap",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.dp))

                HabitHeatmap(
                    mode = if (habit.type == HabitType.CHECK) HeatmapMode.CHECK else HeatmapMode.COUNTER,
                    weeks = 12,
                    accentColor = Color(habit.heatmapColorArgb),
                    valueForDate = { date ->
                        HabitStore.ratioForDate(habit.id, date, habit.type, habit.targetPerDay)
                    }
                )
            }
        }

        // Statistics Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.dp))
                StatisticsRow("Type", habit.type.name)
                Spacer(Modifier.height(8.dp))
                StatisticsRow(
                    "Today",
                    if (habit.doneToday) "Completed" else "Not completed",
                    color = if (habit.doneToday) Color.Green else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                StatisticsRow("Streak", "${habit.streak} days")
            }
        }
    }
}

@Composable
private fun StatisticsRow(label: String, value: String, color: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = color)
    }
}
