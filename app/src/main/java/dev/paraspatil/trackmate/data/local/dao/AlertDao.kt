package dev.paraspatil.trackmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.paraspatil.trackmate.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts ORDER BY timestamp DESC")
    fun observeAllAlerts(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE is_read = 0 ORDER BY timestamp DESC")
    fun observeUnreadAlerts(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE id = :alertId")
    suspend fun getAlertById(alertId: String): AlertEntity?

    @Insert
    suspend fun insertAlert(alert: AlertEntity)

    @Update
    suspend fun updateAlert(alert: AlertEntity)

    @Delete
    suspend fun deleteAlert(alert: AlertEntity)

    @Query("DELETE FROM alerts WHERE id = :alertId")
    suspend fun deleteAlertById(alertId: String)

    @Query("UPDATE alerts SET is_read =1 WHERE id = :alertId")
    suspend fun markAlertAsRead(alertId: String)
}