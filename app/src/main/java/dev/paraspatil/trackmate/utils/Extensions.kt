package dev.paraspatil.trackmate.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(): String =
    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(this))

fun Long.toFormattedTime(): String =
    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(this))

fun Long.toFormattedDateTime(): String =
    SimpleDateFormat("MMM dd  HH:mm", Locale.getDefault()).format(Date(this))

fun Long.timeAgo(): String {
    val diff = System.currentTimeMillis() - this
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        else -> toFormattedDate()
    }
}