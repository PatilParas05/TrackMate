package dev.paraspatil.trackmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.paraspatil.trackmate.data.local.entity.TeamMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamMemberDao {
    @Query("SELECT * FROM team_members ORDER BY name ASC")
    fun observeAllMembers(): Flow<List<TeamMemberEntity>>

    @Query("SELECT * FROM team_members WHERE id = :memberId")
    suspend fun getMemberById(memberId: String): TeamMemberEntity?

    @Insert
    suspend fun insertMember(member: TeamMemberEntity)

    @Update
    suspend fun updateMember(member: TeamMemberEntity)

    @Query("DELETE FROM team_members WHERE id = :memberId")
    suspend fun deleteMember(memberId: String)
}