package com.example.habit_tracker_kotlin.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.example.habit_tracker_kotlin.model.UserProfile
import com.google.firebase.auth.FirebaseAuth

object AuthStore {
    private val auth = FirebaseAuth.getInstance()

    val isAuthenticated = mutableStateOf(auth.currentUser != null)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            isAuthenticated.value = user != null
            if (user != null) {
                UserStore.loadProfile()
                HabitStore.startListening()
            } else {
                UserStore.clear()
                HabitStore.clear()
            }
        }
    }

    val uid: String?
        get() = auth.currentUser?.uid

    fun login(email: String, password: String) {
        isLoading.value = true
        errorMessage.value = ""
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                isLoading.value = false
            }
            .addOnFailureListener { e ->
                isLoading.value = false
                errorMessage.value = e.localizedMessage ?: "Login failed"
            }
    }

    fun register(email: String, password: String, firstName: String, lastName: String) {
        isLoading.value = true
        errorMessage.value = ""
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                isLoading.value = false
                UserStore.saveProfile(UserProfile(firstName = firstName, lastName = lastName, email = email))
            }
            .addOnFailureListener { e ->
                isLoading.value = false
                errorMessage.value = e.localizedMessage ?: "Registration failed"
            }
    }

    fun logout() {
        auth.signOut()
    }
}