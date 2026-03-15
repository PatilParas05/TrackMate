package dev.paraspatil.trackmate.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TeamMember(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val profileImage: String ="",
    val isOnline: Boolean = false,
    val lastSeenAt: Long = System.currentTimeMillis()
)