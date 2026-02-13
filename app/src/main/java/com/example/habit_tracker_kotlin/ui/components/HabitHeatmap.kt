package com.example.habit_tracker_kotlin.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate

enum class HeatmapMode {
    CHECK,
    COUNTER
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HabitHeatmap(
    mode: HeatmapMode,
    weeks: Int = 12,
    accentColor: Color = Color(0xFF0D47A1), // ✅ novo
    valueForDate: (LocalDate) -> Float
) {
    val rows = 7
    val gap = 5.dp
    val labelWidth = 30.dp

    val today = LocalDate.now()
    // Start from Monday of the current week, then go back (weeks-1) weeks
    val currentWeekMonday = today.with(DayOfWeek.MONDAY)
    val startDate = currentWeekMonday.minusWeeks((weeks - 1).toLong())

    val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column(modifier = Modifier.fillMaxWidth()) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {

            val gridWidth = maxWidth - labelWidth
            val cellSize = calculateCellSize(
                gridWidth = gridWidth,
                weeks = weeks,
                gap = gap
            )

            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                for (row in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(labelWidth)
                                .height(cellSize),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayLabels[row],
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Start
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(gridWidth)
                                .height(cellSize)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(gap),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (week in 0 until weeks) {
                                    val date = startDate.plusDays((week * rows + row).toLong())
                                    val ratio =
                                        if (date.isAfter(today)) 0f
                                        else valueForDate(date).coerceIn(0f, 1f)

                                    HeatCell(
                                        mode = mode,
                                        ratio = ratio,
                                        size = cellSize,
                                        accentColor = accentColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        when (mode) {
            HeatmapMode.CHECK -> CheckLegend(accentColor)
            HeatmapMode.COUNTER -> CounterLegend(accentColor)
        }
    }
}

@Composable
private fun HeatCell(
    mode: HeatmapMode,
    ratio: Float,
    size: Dp,
    accentColor: Color
) {
    val empty = Color(0xFFB0BEC5)

    val c1 = blend(Color.White, accentColor, 0.35f)
    val c2 = blend(Color.White, accentColor, 0.65f)
    val c3 = accentColor

    val color = when (mode) {
        HeatmapMode.CHECK ->
            if (ratio > 0f) accentColor else empty

        HeatmapMode.COUNTER -> when {
            ratio <= 0f -> empty
            ratio < 0.34f -> c1
            ratio < 0.67f -> c2
            else -> c3
        }
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(color)
    )
}

@Composable
private fun CheckLegend(accentColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendText("Not done")
        Spacer(Modifier.width(6.dp))
        LegendSquare(Color(0xFFB0BEC5))
        Spacer(Modifier.width(12.dp))
        LegendText("Done")
        Spacer(Modifier.width(6.dp))
        LegendSquare(accentColor)
    }
}

@Composable
private fun CounterLegend(accentColor: Color) {
    val c1 = blend(Color.White, accentColor, 0.35f)
    val c2 = blend(Color.White, accentColor, 0.65f)
    val c3 = accentColor

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendText("Less")
        Spacer(Modifier.width(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            LegendSquare(Color(0xFFB0BEC5))
            LegendSquare(c1)
            LegendSquare(c2)
            LegendSquare(c3)
        }
        Spacer(Modifier.width(6.dp))
        LegendText("More")
    }
}

@Composable
private fun LegendSquare(color: Color) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(color)
    )
}

@Composable
private fun LegendText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private fun calculateCellSize(
    gridWidth: Dp,
    weeks: Int,
    gap: Dp
): Dp {
    return (gridWidth - gap * (weeks - 1)) / weeks
}

private fun blend(from: Color, to: Color, t: Float): Color {
    val clamped = t.coerceIn(0f, 1f)
    return Color(
        red = from.red + (to.red - from.red) * clamped,
        green = from.green + (to.green - from.green) * clamped,
        blue = from.blue + (to.blue - from.blue) * clamped,
        alpha = 1f
    )
}
