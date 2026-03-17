package dev.paraspatil.trackmate.data.repository

import dev.paraspatil.trackmate.data.local.dao.AlertDao
import dev.paraspatil.trackmate.data.local.entity.AlertEntity
import dev.paraspatil.trackmate.domain.model.Alert
import dev.paraspatil.trackmate.domain.model.Result
import dev.paraspatil.trackmate.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val alertDao: AlertDao
) : AlertRepository{

    override fun observeAlerts(): Flow<Result<List<Alert>>> =
        alertDao.observeAllAlerts()
            .map { alerts ->
                Result.Success(alerts.map { it.toDomain() }) as Result<List<Alert>>
            }
            .catch { emit(Result.Error(it)) }

    override fun observeUnreadAlerts(): Flow<Result<List<Alert>>> =
        alertDao.observeUnreadAlerts()
            .map { alerts ->
                Result.Success(alerts.map { it.toDomain() }) as Result<List<Alert>>
            }
            .catch { emit(Result.Error(it)) }

    override suspend fun sendAlert(alert: Alert): Result<Unit> {
        return try {
            alertDao.insertAlert(AlertEntity.fromDomain(alert))
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun markAlertAsRead(alertId: String): Result<Unit> {
        return try {
            alertDao.markAlertAsRead(alertId)
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun deleteAlert(alertId: String): Result<Unit> {
        return try {
            alertDao.deleteAlertById(alertId)
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

}