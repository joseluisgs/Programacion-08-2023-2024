package dev.joseluisgs.models

import java.time.LocalDateTime

data class Producto(
    val id: Long = -1,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categoria: Categoria,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
)

enum class Categoria(val categoria: String) {
    ELECTRONICA("ELECTRONICA"),
    DEPORTE("DEPORTE"),
    MODA("MODA"),
    OTROS("OTROS")
}