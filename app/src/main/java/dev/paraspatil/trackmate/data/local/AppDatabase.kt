package dev.paraspatil.trackmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.paraspatil.trackmate.data.local.dao.AlertDao
import dev.paraspatil.trackmate.data.local.dao.TaskDao
import dev.paraspatil.trackmate.data.local.dao.TeamMemberDao
import dev.paraspatil.trackmate.data.local.entity.AlertEntity
import dev.paraspatil.trackmate.data.local.entity.TaskEntity
import dev.paraspatil.trackmate.data.local.entity.TeamMemberEntity

@Database(
    entities = [TaskEntity::class, AlertEntity::class, TeamMemberEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun taskDao(): TaskDao
    abstract fun alertDao(): AlertDao
    abstract fun teamMemberDao(): TeamMemberDao
}
