package com.example.habit_tracker_kotlin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.habit_tracker_kotlin.model.HabitType
import com.example.habit_tracker_kotlin.ui.components.HabitIcon

@Composable
fun CreateHabitScreen(
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var iconId by remember { mutableStateOf("water") }
    var type by remember { mutableStateOf(HabitType.CHECK) }
    var targetPerDay by remember { mutableStateOf("1") }
    var colorArgb by remember { mutableIntStateOf(0xFF0D47A1.toInt()) }

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
            Text("New habit", style = MaterialTheme.typography.titleLarge)
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
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("e.g. Drink water") }
                    )
                }
            }

            // Type Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("Type", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilterChip(
                            selected = type == HabitType.CHECK,
                            onClick = { type = HabitType.CHECK; targetPerDay = "1" },
                            label = { Text("Check") }
                        )
                        FilterChip(
                            selected = type == HabitType.COUNTER,
                            onClick = { type = HabitType.COUNTER },
                            label = { Text("Counter") }
                        )
                    }
                    if (type == HabitType.COUNTER) {
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = targetPerDay,
                            onValueChange = { targetPerDay = it.filter { c -> c.isDigit() } },
                            label = { Text("Target per day") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
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

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                        0xFF0D47A1.toInt(),
                        0xFF1E88E5.toInt(),
                        0xFF1976D2.toInt(),
                        0xFF2E7D32.toInt(),
                        0xFF388E3C.toInt(),
                        0xFF43A047.toInt(),
                        0xFFC62828.toInt(),
                        0xFFD32F2F.toInt(),
                        0xFFE53935.toInt(),
                        0xFF6A1B9A.toInt(),
                        0xFF7B1FA2.toInt(),
                        0xFF8E24AA.toInt(),
                        0xFFE65100.toInt(),
                        0xFFF57C00.toInt(),
                        0xFFFF6F00.toInt(),
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                        if (name.isBlank()) return@Button
                        val target = targetPerDay.toIntOrNull() ?: 1
                        HabitStore.createHabit(
                            name = name,
                            iconId = iconId,
                            type = type,
                            targetPerDay = if (type == HabitType.CHECK) 1 else target.coerceAtLeast(1),
                            heatmapColorArgb = colorArgb
                        )
                        onBack()
                    }
                ) { Text("Create") }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
