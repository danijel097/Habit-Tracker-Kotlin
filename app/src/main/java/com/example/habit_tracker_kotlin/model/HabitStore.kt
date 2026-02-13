package com.example.habit_tracker_kotlin.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object HabitStore {
    private val db = FirebaseFirestore.getInstance()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val habits = mutableStateListOf<Habit>()
    val logCache = mutableStateMapOf<String, HabitLog>() // key: "{habitId}_{date}"

    private var habitsListener: ListenerRegistration? = null
    private var logsListener: ListenerRegistration? = null

    fun getById(id: String): Habit? = habits.firstOrNull { it.id == id }

    fun startListening() {
        val uid = AuthStore.uid ?: return
        stopListening()

        // Listen for habit documents
        habitsListener = db.collection("habits").document(uid).collection("items")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener
                val today = LocalDate.now().format(dateFormatter)
                val newHabits = snapshot.documents.mapNotNull { doc ->
                    val type = try {
                        HabitType.valueOf(doc.getString("type") ?: "CHECK")
                    } catch (_: Exception) {
                        HabitType.CHECK
                    }
                    val id = doc.id
                    val todayLog = logCache["${id}_$today"]
                    Habit(
                        id = id,
                        name = doc.getString("name") ?: "",
                        iconId = doc.getString("iconId") ?: "water",
                        type = type,
                        targetPerDay = (doc.getLong("targetPerDay") ?: 1).toInt(),
                        heatmapColorArgb = (doc.getLong("heatmapColorArgb") ?: 0xFF0D47A1).toInt(),
                        doneToday = todayLog?.done ?: false,
                        countToday = todayLog?.count ?: 0,
                        streak = computeStreak(id, type)
                    )
                }
                habits.clear()
                habits.addAll(newHabits)
            }

        // Listen for today's logs
        val today = LocalDate.now().format(dateFormatter)
        logsListener = db.collection("habitLogs").document(uid).collection("entries")
            .whereGreaterThanOrEqualTo("date", today)
            .whereLessThanOrEqualTo("date", today)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener
                for (doc in snapshot.documents) {
                    val habitId = doc.getString("habitId") ?: continue
                    val date = doc.getString("date") ?: continue
                    val type = try {
                        HabitType.valueOf(doc.getString("type") ?: "CHECK")
                    } catch (_: Exception) {
                        HabitType.CHECK
                    }
                    val log = HabitLog(
                        habitId = habitId,
                        date = date,
                        type = type,
                        done = doc.getBoolean("done") ?: false,
                        count = (doc.getLong("count") ?: 0).toInt(),
                        target = (doc.getLong("target") ?: 1).toInt()
                    )
                    logCache["${habitId}_$date"] = log
                }
                // Refresh today's status on habits
                refreshTodayStatus()
            }

        // Pre-fetch 12 weeks of logs for heatmap
        loadLogsForRange(uid, 12)
    }

    private fun stopListening() {
        habitsListener?.remove()
        habitsListener = null
        logsListener?.remove()
        logsListener = null
    }

    fun loadLogsForRange(uid: String? = AuthStore.uid, weeks: Int = 12) {
        val resolvedUid = uid ?: return
        val end = LocalDate.now()
        val start = end.minusWeeks(weeks.toLong())
        db.collection("habitLogs").document(resolvedUid).collection("entries")
            .whereGreaterThanOrEqualTo("date", start.format(dateFormatter))
            .whereLessThanOrEqualTo("date", end.format(dateFormatter))
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    val habitId = doc.getString("habitId") ?: continue
                    val date = doc.getString("date") ?: continue
                    val type = try {
                        HabitType.valueOf(doc.getString("type") ?: "CHECK")
                    } catch (_: Exception) {
                        HabitType.CHECK
                    }
                    logCache["${habitId}_$date"] = HabitLog(
                        habitId = habitId,
                        date = date,
                        type = type,
                        done = doc.getBoolean("done") ?: false,
                        count = (doc.getLong("count") ?: 0).toInt(),
                        target = (doc.getLong("target") ?: 1).toInt()
                    )
                }
                refreshTodayStatus()
            }
    }

    fun ratioForDate(habitId: String, date: LocalDate, type: HabitType, target: Int): Float {
        val key = "${habitId}_${date.format(dateFormatter)}"
        val log = logCache[key] ?: return 0f
        return when (type) {
            HabitType.CHECK -> if (log.done) 1f else 0f
            HabitType.COUNTER -> if (target <= 0) 0f else (log.count.toFloat() / target).coerceIn(0f, 1f)
        }
    }

    private fun refreshTodayStatus() {
        val today = LocalDate.now().format(dateFormatter)
        val updated = habits.map { habit ->
            val log = logCache["${habit.id}_$today"]
            habit.copy(
                doneToday = log?.done ?: false,
                countToday = log?.count ?: 0,
                streak = computeStreak(habit.id, habit.type)
            )
        }
        habits.clear()
        habits.addAll(updated)
    }

    private fun computeStreak(habitId: String, type: HabitType): Int {
        var streak = 0
        var date = LocalDate.now()
        while (true) {
            val key = "${habitId}_${date.format(dateFormatter)}"
            val log = logCache[key]
            val completed = when (type) {
                HabitType.CHECK -> log?.done ?: false
                HabitType.COUNTER -> log != null && log.target > 0 && log.count >= log.target
            }
            if (!completed) {
                // Allow today to be incomplete without breaking streak
                if (date == LocalDate.now()) {
                    date = date.minusDays(1)
                    continue
                }
                break
            }
            streak++
            date = date.minusDays(1)
        }
        return streak
    }

    private fun writeTodayLog(habit: Habit, done: Boolean, count: Int) {
        val uid = AuthStore.uid ?: return
        val today = LocalDate.now().format(dateFormatter)
        val docId = "${habit.id}_$today"
        val log = HabitLog(
            habitId = habit.id,
            date = today,
            type = habit.type,
            done = done,
            count = count,
            target = habit.targetPerDay
        )
        logCache[docId] = log
        db.collection("habitLogs").document(uid).collection("entries").document(docId)
            .set(
                mapOf(
                    "habitId" to habit.id,
                    "date" to today,
                    "type" to habit.type.name,
                    "done" to done,
                    "count" to count,
                    "target" to habit.targetPerDay
                )
            )
    }

    fun toggleDone(id: String) {
        val h = getById(id) ?: return
        val newDone = !h.doneToday
        val idx = habits.indexOfFirst { it.id == id }
        if (idx >= 0) habits[idx] = h.copy(doneToday = newDone)
        writeTodayLog(h, done = newDone, count = h.countToday)
    }

    fun incCounter(id: String) {
        val h = getById(id) ?: return
        val newCount = (h.countToday + 1).coerceAtMost(h.targetPerDay)
        val idx = habits.indexOfFirst { it.id == id }
        if (idx >= 0) habits[idx] = h.copy(countToday = newCount)
        writeTodayLog(h, done = h.doneToday, count = newCount)
    }

    fun decCounter(id: String) {
        val h = getById(id) ?: return
        val newCount = (h.countToday - 1).coerceAtLeast(0)
        val idx = habits.indexOfFirst { it.id == id }
        if (idx >= 0) habits[idx] = h.copy(countToday = newCount)
        writeTodayLog(h, done = h.doneToday, count = newCount)
    }

    fun deleteHabit(id: String) {
        habits.removeAll { it.id == id }
        val uid = AuthStore.uid ?: return
        db.collection("habits").document(uid).collection("items").document(id).delete()
    }

    fun updateHabit(updated: Habit) {
        val idx = habits.indexOfFirst { it.id == updated.id }
        if (idx >= 0) habits[idx] = updated
        val uid = AuthStore.uid ?: return
        db.collection("habits").document(uid).collection("items").document(updated.id)
            .set(
                mapOf(
                    "name" to updated.name,
                    "iconId" to updated.iconId,
                    "type" to updated.type.name,
                    "targetPerDay" to updated.targetPerDay,
                    "heatmapColorArgb" to updated.heatmapColorArgb
                )
            )
    }

    fun createHabit(name: String, iconId: String, type: HabitType, targetPerDay: Int, heatmapColorArgb: Int) {
        val uid = AuthStore.uid ?: return
        db.collection("habits").document(uid).collection("items").add(
            mapOf(
                "name" to name,
                "iconId" to iconId,
                "type" to type.name,
                "targetPerDay" to targetPerDay,
                "heatmapColorArgb" to heatmapColorArgb
            )
        )
    }

    fun clear() {
        stopListening()
        habits.clear()
        logCache.clear()
    }
}
