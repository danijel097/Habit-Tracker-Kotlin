package com.example.habit_tracker_kotlin.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.example.habit_tracker_kotlin.viewmodel.AuthStore
import com.example.habit_tracker_kotlin.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

object UserStore {
    private val db = FirebaseFirestore.getInstance()

    val profile = mutableStateOf(UserProfile(firstName = "", lastName = "", email = ""))

    fun loadProfile() {
        val uid = AuthStore.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    profile.value = UserProfile(
                        uid = uid,
                        firstName = doc.getString("firstName") ?: "",
                        lastName = doc.getString("lastName") ?: "",
                        email = doc.getString("email") ?: "",
                        isAdmin = doc.getString("role") == "admin"
                    )
                }
            }
    }

    fun saveProfile(userProfile: UserProfile) {
        val uid = AuthStore.uid ?: return
        profile.value = userProfile.copy(uid = uid)
        db.collection("users").document(uid).set(
            mapOf(
                "firstName" to userProfile.firstName,
                "lastName" to userProfile.lastName,
                "email" to userProfile.email,
                "role" to if (userProfile.isAdmin) "admin" else "user"
            )
        )
    }

    fun updateProfile(updated: UserProfile) {
        val uid = AuthStore.uid ?: return
        profile.value = updated.copy(uid = uid)
        db.collection("users").document(uid).set(
            mapOf(
                "firstName" to updated.firstName,
                "lastName" to updated.lastName,
                "email" to updated.email,
                "role" to if (updated.isAdmin) "admin" else "user"
            )
        )
    }

    fun clear() {
        profile.value = UserProfile(firstName = "", lastName = "", email = "")
    }
}