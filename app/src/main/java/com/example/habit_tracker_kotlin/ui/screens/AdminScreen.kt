package com.example.habit_tracker_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_kotlin.model.UserProfile
import com.example.habit_tracker_kotlin.viewmodel.AdminStore
import com.example.habit_tracker_kotlin.viewmodel.AuthStore

@Composable
fun AdminScreen() {
    val currentUid = AuthStore.uid
    val users = AdminStore.users
    val isLoading = AdminStore.isLoading.value
    val errorMessage = AdminStore.errorMessage.value

    var userToDelete by remember { mutableStateOf<UserProfile?>(null) }

    LaunchedEffect(Unit) {
        AdminStore.loadAllUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Admin Panel", style = MaterialTheme.typography.headlineMedium)

        if (errorMessage.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(users, key = { it.uid }) { user ->
                    UserCard(
                        user = user,
                        isCurrentUser = user.uid == currentUid,
                        onDelete = { userToDelete = user },
                        onToggleAdmin = { AdminStore.setAdmin(user.uid, !user.isAdmin) }
                    )
                }
            }
        }
    }

    if (userToDelete != null) {
        val user = userToDelete!!
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = { Text("Obriši usera") },
            text = {
                Text("Da li sigurno želiš da obrišeš ${user.firstName} ${user.lastName}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        AdminStore.deleteUser(user.uid)
                        userToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Obriši")
                }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) {
                    Text("Otkaži")
                }
            }
        )
    }
}

@Composable
private fun UserCard(
    user: UserProfile,
    isCurrentUser: Boolean,
    onDelete: () -> Unit,
    onToggleAdmin: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (isCurrentUser) {
                        Badge { Text("ti") }
                    }
                }
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onToggleAdmin,
                enabled = !isCurrentUser
            ) {
                Icon(
                    imageVector = if (user.isAdmin) Icons.Filled.Shield else Icons.Outlined.Shield,
                    contentDescription = if (user.isAdmin) "Ukloni admin" else "Postavi admin",
                    tint = if (user.isAdmin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onDelete,
                enabled = !isCurrentUser
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Obriši usera",
                    tint = if (isCurrentUser) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}