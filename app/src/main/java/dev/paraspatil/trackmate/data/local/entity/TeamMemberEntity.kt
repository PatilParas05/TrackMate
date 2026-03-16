package dev.paraspatil.trackmate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.paraspatil.trackmate.domain.model.TeamMember

@Entity(tableName = "team_members")
data class TeamMemberEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String="",
    @ColumnInfo(name = "is_online")
    val isOnline: Boolean=false,
    @ColumnInfo(name = "last_seen_at")
    val lastSeenAt: Long=System.currentTimeMillis()
){
    fun toDomain(): TeamMember = TeamMember(
        id = id,
        name = name,
        email = email,
        role = role,
        profileImage = profileImageUrl,
        isOnline = isOnline,
        lastSeenAt = lastSeenAt
    )
    companion object{
        fun fromDomain(teamMember: TeamMember): TeamMemberEntity = TeamMemberEntity(
            id = teamMember.id,
            name = teamMember.name,
            email = teamMember.email,
            role = teamMember.role,
            profileImageUrl = teamMember.profileImage,
            isOnline = teamMember.isOnline,
            lastSeenAt = teamMember.lastSeenAt
        )
    }
}
