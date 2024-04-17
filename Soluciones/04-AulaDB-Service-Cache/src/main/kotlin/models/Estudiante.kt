package dev.joseluisgs.models

import java.time.LocalDate


class Estudiante(
    id: Long,
    nombre: String,
    val edad: Int,
    createdAt: LocalDate = LocalDate.now(),
    updatedAt: LocalDate? = null,
    isDeleted: Boolean? = false
) : Persona(id, nombre, createdAt, updatedAt, isDeleted) {
    override fun toString(): String {
        return "Estudiante(id=$id, nombre=$nombre, edad=$edad, createdAt=$createdAt, updatedAt=$updatedAt, isDeleted=$isDeleted)"
    }
}