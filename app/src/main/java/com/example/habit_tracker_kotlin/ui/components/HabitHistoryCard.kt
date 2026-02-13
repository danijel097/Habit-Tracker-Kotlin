package com.example.habit_tracker_kotlin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.model.Habit
import com.example.habit_tracker_kotlin.model.HabitType
import java.time.LocalDate

@Composable
fun HabitHistoryCard(
    habit: Habit,
    onClick: () -> Unit,
    valueForDate: (LocalDate) -> Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                HabitIcon(habit.iconId)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(habit.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = if (habit.type == HabitType.CHECK) "Once per day" else "Multiple per day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // 👇 Ovo je tvoj heatmap (7x12)
            HabitHeatmap(
                mode = if (habit.type == HabitType.CHECK) HeatmapMode.CHECK else HeatmapMode.COUNTER,
                weeks = 12,
                valueForDate = valueForDate,
                accentColor = Color(habit.heatmapColorArgb),
            )
        }
    }
}
