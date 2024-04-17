package dev.joseluisgs.models

import java.time.LocalDate

abstract class Persona(
    val id: Long = -1,
    val nombre: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate?,
    val isDeleted: Boolean?
)

