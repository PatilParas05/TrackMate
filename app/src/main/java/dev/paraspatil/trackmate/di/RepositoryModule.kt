package dev.paraspatil.trackmate.di

import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.paraspatil.trackmate.data.local.dao.AlertDao
import dev.paraspatil.trackmate.data.local.dao.TaskDao
import dev.paraspatil.trackmate.data.repository.AlertRepositoryImpl
import dev.paraspatil.trackmate.data.repository.LocationRepositoryImpl
import dev.paraspatil.trackmate.data.repository.TaskRepositoryImpl
import dev.paraspatil.trackmate.domain.repository.AlertRepository
import dev.paraspatil.trackmate.domain.repository.LocationRepository
import dev.paraspatil.trackmate.domain.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLocationRepository(
        fusedLocationClient: FusedLocationProviderClient
    ): LocationRepository{
        return LocationRepositoryImpl(fusedLocationClient)
    }
    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao
    ): TaskRepository{
        return TaskRepositoryImpl(taskDao)
    }
    @Provides
    @Singleton
    fun provideAlertRepository(
        alertDao: AlertDao
    ): AlertRepository {
        return AlertRepositoryImpl(alertDao)
    }

}