package dev.paraspatil.trackmate.ui.alert

import dev.paraspatil.trackmate.domain.model.Alert

sealed class AlertIntent {
    object LoadAlerts : AlertIntent()
    data class SendAlert(val alert: Alert) : AlertIntent()
    data class MarkAsRead(val alertId: String) : AlertIntent()
    data class DeleteAlert(val alertId: String) : AlertIntent()
    object ClearError : AlertIntent()
}
