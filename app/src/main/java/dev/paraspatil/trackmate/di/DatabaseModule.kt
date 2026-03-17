package dev.paraspatil.trackmate.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.paraspatil.trackmate.data.local.AppDatabase
import dev.paraspatil.trackmate.data.local.dao.AlertDao
import dev.paraspatil.trackmate.data.local.dao.TaskDao
import dev.paraspatil.trackmate.data.local.dao.TeamMemberDao
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
       return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "trackmate_db"
        ).build()
    }
    @Provides
    @Singleton
    fun provideTaskDao(database : AppDatabase): TaskDao {
        return database.taskDao()
    }
    @Provides
    @Singleton
    fun provideAlertDao(database : AppDatabase): AlertDao {
        return database.alertDao()
    }
    @Provides
    @Singleton
    fun provideTeamDao(database : AppDatabase): TeamMemberDao {
        return database.teamMemberDao()
    }
}