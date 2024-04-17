package dev.joseluisgs.mappers

import database.ClienteEntity
import dev.joseluisgs.models.Cliente
import java.time.LocalDateTime

fun ClienteEntity.toCliente(): Cliente {
    return Cliente(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        createdAt = LocalDateTime.parse(this.created_at),
        updatedAt = LocalDateTime.parse(this.updated_at),
        isDeleted = this.is_deleted.toInt() == 1
    )
}