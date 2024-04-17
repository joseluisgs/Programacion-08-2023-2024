package dev.joseluisgs.clientes.models

import java.time.LocalDateTime

data class Cliente(
    val id: Long,
    val nombre: String,
    val email: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
)