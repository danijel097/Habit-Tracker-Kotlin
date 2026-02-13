package com.example.habit_tracker_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.AppSettings
import com.example.habit_tracker_kotlin.model.AuthStore
import com.example.habit_tracker_kotlin.model.HabitStore
import com.example.habit_tracker_kotlin.model.HabitType
import com.example.habit_tracker_kotlin.model.UserStore

@Composable
fun AccountScreen(
    onEdit: () -> Unit
) {
    val profile = UserStore.profile.value
    val darkModeEnabled = rememberSaveable { AppSettings.darkMode }

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
            Text("Account", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit account")
            }
        }

        // 1. Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Profile", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                InfoRow("First Name", profile.firstName)
                InfoRow("Last Name", profile.lastName)
                InfoRow("Email", profile.email)
            }
        }

        // 2. Statistics Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Statistics", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                val habits = HabitStore.habits
                val totalHabits = habits.size
                val completedToday = habits.count { habit ->
                    when (habit.type) {
                        HabitType.CHECK -> habit.doneToday
                        HabitType.COUNTER -> habit.countToday >= habit.targetPerDay
                    }
                }
                val longestStreak = if (habits.isEmpty()) 0 else habits.maxOf { it.streak }

                StatRow("Total Habits", "$totalHabits")
                StatRow("Completed Today", "$completedToday / $totalHabits")
                StatRow("Longest Streak", "$longestStreak days")
            }
        }

        // 3. Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Settings", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dark Mode",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = darkModeEnabled.value,
                        onCheckedChange = { isChecked ->
                            darkModeEnabled.value = isChecked
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        OutlinedButton(
            onClick = { AuthStore.logout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}