package dev.joseluisgs.locale

import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDateTime.toDefaultDateTimeString(): String {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(this)
}

fun Double.toDefaultMoneyString(): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
}
