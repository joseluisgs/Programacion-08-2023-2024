package dev.joseluisgs.models

import java.time.LocalDateTime

data class Cliente(
    val id: Long = -1,
    val nombre: String,
    val email: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
)