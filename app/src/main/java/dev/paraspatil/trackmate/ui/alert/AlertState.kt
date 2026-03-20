package dev.paraspatil.trackmate.ui.alert

import dev.paraspatil.trackmate.domain.model.Alert
import dev.paraspatil.trackmate.domain.model.Task

data class AlertState (
    val alerts: List<Alert> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class AlertEffect {
    data class ShowMessage(val message: String) : AlertEffect()
    object AlertSent : AlertEffect()
    object AlertDeleted : AlertEffect()
}