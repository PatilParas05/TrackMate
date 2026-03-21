package dev.paraspatil.trackmate.ui.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.paraspatil.trackmate.data.local.entity.AlertEntity
import dev.paraspatil.trackmate.domain.model.Alert
import dev.paraspatil.trackmate.domain.repository.AlertRepository
import dev.paraspatil.trackmate.domain.model.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val alertRepository: AlertRepository
) : ViewModel(){
    private val _state = MutableStateFlow(AlertState())
    val state: StateFlow<AlertState> = _state

    private val _effect = MutableSharedFlow<AlertEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadAlerts()
    }


    fun processIntent(intent: AlertIntent) {
        when (intent) {
            is AlertIntent.LoadAlerts -> loadAlerts()
            is AlertIntent.SendAlert -> sendAlert(intent.alert)
            is AlertIntent.MarkAsRead -> markAlertAsRead(intent.alertId)
            is AlertIntent.DeleteAlert -> deleteAlert(intent.alertId)
            is AlertIntent.ClearError -> clearError()
        }
    }
    private fun loadAlerts() {
        viewModelScope.launch {
            _state.value =_state.value.copy(isLoading = true)
            alertRepository.observeAlerts().collect { result ->
                when (result) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        alerts = result.data,
                        unreadCount = result.data.count { !it.isRead },
                        isLoading = false,
                        error = null
                    )
                }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun sendAlert(alert: Alert){
        viewModelScope.launch {
        when(alertRepository.sendAlert(alert)) {
            is Result.Success -> {
                _effect.emit(AlertEffect.AlertSent)
                loadAlerts()
            }
            is Result.Error -> {
                _effect.emit(AlertEffect.ShowMessage("Failed to send alert"))
            }
            else -> {}
            }
        }
    }

    private  fun markAlertAsRead(alertId: String) {
    viewModelScope.launch {
        when(alertRepository.markAlertAsRead(alertId)) {
            is Result.Success -> {
                loadAlerts()
            }
            is Result.Error -> {
                _effect.emit(AlertEffect.ShowMessage("Failed to mark alert as read"))
            }
            else -> {}
            }
        }
    }
    private fun deleteAlert(alertId: String) {
    viewModelScope.launch {
        when(alertRepository.deleteAlert(alertId)) {
            is Result.Success -> {
                loadAlerts()
            }
            is Result.Error -> {
                _effect.emit(AlertEffect.ShowMessage("Failed to delete alert"))
            }
            else -> {}
            }
        }
    }
    private fun clearError(){
        _state.value = _state.value.copy(error = null)
    }
}
