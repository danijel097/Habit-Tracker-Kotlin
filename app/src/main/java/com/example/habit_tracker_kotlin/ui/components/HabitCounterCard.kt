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
fun HabitCounterCard(
    habit: Habit,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
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
                        "Today: ${habit.countToday}/${habit.targetPerDay}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onMinus,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) { Text("-") }

                Spacer(Modifier.width(10.dp))

                Text(habit.countToday.toString(), style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.width(10.dp))

                Button(
                    onClick = onPlus,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) { Text("+") }
            }
        }
    }
}
