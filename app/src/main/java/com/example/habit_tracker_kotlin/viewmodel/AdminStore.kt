package com.example.habit_tracker_kotlin.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.habit_tracker_kotlin.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

object AdminStore {
    private val db = FirebaseFirestore.getInstance()

    val users = mutableStateListOf<UserProfile>()
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    fun loadAllUsers() {
        isLoading.value = true
        errorMessage.value = ""
        db.collection("users").get()
            .addOnSuccessListener { snapshot ->
                users.clear()
                snapshot.documents.forEach { doc ->
                    users.add(
                        UserProfile(
                            uid = doc.id,
                            firstName = doc.getString("firstName") ?: "",
                            lastName = doc.getString("lastName") ?: "",
                            email = doc.getString("email") ?: "",
                            isAdmin = doc.getString("role") == "admin"
                        )
                    )
                }
                isLoading.value = false
            }
            .addOnFailureListener { e ->
                isLoading.value = false
                errorMessage.value = e.localizedMessage ?: "Failed to load users"
            }
    }

    fun deleteUser(uid: String) {
        db.collection("users").document(uid).delete()
            .addOnSuccessListener {
                users.removeAll { it.uid == uid }
            }
            .addOnFailureListener { e ->
                errorMessage.value = e.localizedMessage ?: "Failed to delete user"
            }
    }

    fun setAdmin(uid: String, isAdmin: Boolean) {
        db.collection("users").document(uid).update("role", if (isAdmin) "admin" else "user")
            .addOnSuccessListener {
                val index = users.indexOfFirst { it.uid == uid }
                if (index != -1) {
                    users[index] = users[index].copy(isAdmin = isAdmin)
                }
            }
            .addOnFailureListener { e ->
                errorMessage.value = e.localizedMessage ?: "Failed to update admin status"
            }
    }
}