package dev.paraspatil.trackmate.domain.repository

import dev.paraspatil.trackmate.domain.model.Alert
import kotlinx.coroutines.flow.Flow
import dev.paraspatil.trackmate.domain.model.Result

interface AlertRepository {
    fun observeAlerts(): Flow<Result<List<Alert>>>
    fun observeUnreadAlerts(): Flow<Result<List<Alert>>>
    suspend fun sendAlert(alert: Alert): Result<Unit>
    suspend fun markAlertAsRead(alertId: String): Result<Unit>
    suspend fun deleteAlert(alertId: String): Result<Unit>

}