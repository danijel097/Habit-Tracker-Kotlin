package com.example.habit_tracker_kotlin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.model.HabitStore
import com.example.habit_tracker_kotlin.ui.components.HabitIcon

@Composable
fun EditHabitScreen(
    habitId: String,
    onBack: () -> Unit
) {
    val habit = HabitStore.getById(habitId)

    if (habit == null) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Habit not found (id=$habitId)")
        }
        return
    }

    var name by remember { mutableStateOf(habit.name) }
    var iconId by remember { mutableStateOf(habit.iconId) }
    var colorArgb by remember { mutableStateOf(habit.heatmapColorArgb) }

    Column(Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(6.dp))
            Text("Edit habit", style = MaterialTheme.typography.titleLarge)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Name Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("Name", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { newValue -> name = newValue },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            // Icon picker Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Icon", style = MaterialTheme.typography.titleMedium)

                    val icons = listOf(
                        "water", "gym", "book", "sleep", "study", "run",
                        "meditation", "food", "coffee", "music", "work", "home"
                    )

                    // Fixed height grid (bez user scroll-a)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        icons.chunked(6).forEach { rowIcons ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                rowIcons.forEach { id ->
                                    val selected = id == iconId
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(MaterialTheme.shapes.large)
                                            .background(
                                                if (selected) MaterialTheme.colorScheme.primaryContainer
                                                else MaterialTheme.colorScheme.surface
                                            )
                                            .border(
                                                width = if (selected) 2.dp else 1.dp,
                                                color = if (selected) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.outline,
                                                shape = MaterialTheme.shapes.large
                                            )
                                            .clickable { iconId = id }
                                            .padding(10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        HabitIcon(id)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Color picker Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Heatmap color", style = MaterialTheme.typography.titleMedium)

                    val colors = listOf(
                        0xFF0D47A1.toInt(), // Dark Blue
                        0xFF1E88E5.toInt(), // Blue
                        0xFF1976D2.toInt(), // Medium Blue
                        0xFF2E7D32.toInt(), // Green
                        0xFF388E3C.toInt(), // Light Green
                        0xFF43A047.toInt(), // Lighter Green
                        0xFFC62828.toInt(), // Red
                        0xFFD32F2F.toInt(), // Light Red
                        0xFFE53935.toInt(), // Lighter Red
                        0xFF6A1B9A.toInt(), // Purple
                        0xFF7B1FA2.toInt(), // Light Purple
                        0xFF8E24AA.toInt(), // Lighter Purple
                        0xFFE65100.toInt(), // Orange
                        0xFFF57C00.toInt(), // Light Orange
                        0xFFFF6F00.toInt(), // Amber
                    )

                    // Fixed height grid (bez user scroll-a)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        colors.chunked(5).forEach { rowColors ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                rowColors.forEach { argb ->
                                    val selected = argb == colorArgb
                                    Box(
                                        modifier = Modifier
                                            .size(59.dp)
                                            .clip(CircleShape)
                                            .background(Color(argb))
                                            .border(
                                                width = if (selected) 3.dp else 0.dp,
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = CircleShape
                                            )
                                            .clickable { colorArgb = argb },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (selected) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Selected",
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Save / Cancel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onBack
                ) { Text("Cancel") }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (name.isBlank()) {
                            // Add validation to avoid empty name
                            return@Button
                        }
                        HabitStore.updateHabit(
                            habit.copy(
                                name = name,
                                iconId = iconId,
                                heatmapColorArgb = colorArgb
                            )
                        )
                        onBack()
                    }
                ) { Text("Save") }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}