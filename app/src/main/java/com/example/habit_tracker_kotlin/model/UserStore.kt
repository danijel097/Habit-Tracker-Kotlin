package com.example.habit_tracker_kotlin.model

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore

object UserStore {
    private val db = FirebaseFirestore.getInstance()

    val profile = mutableStateOf(UserProfile("", "", ""))

    fun loadProfile() {
        val uid = AuthStore.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    profile.value = UserProfile(
                        firstName = doc.getString("firstName") ?: "",
                        lastName = doc.getString("lastName") ?: "",
                        email = doc.getString("email") ?: ""
                    )
                }
            }
    }

    fun saveProfile(userProfile: UserProfile) {
        val uid = AuthStore.uid ?: return
        profile.value = userProfile
        db.collection("users").document(uid).set(
            mapOf(
                "firstName" to userProfile.firstName,
                "lastName" to userProfile.lastName,
                "email" to userProfile.email
            )
        )
    }

    fun updateProfile(updated: UserProfile) {
        profile.value = updated
        val uid = AuthStore.uid ?: return
        db.collection("users").document(uid).set(
            mapOf(
                "firstName" to updated.firstName,
                "lastName" to updated.lastName,
                "email" to updated.email
            )
        )
    }

    fun clear() {
        profile.value = UserProfile("", "", "")
    }
}
