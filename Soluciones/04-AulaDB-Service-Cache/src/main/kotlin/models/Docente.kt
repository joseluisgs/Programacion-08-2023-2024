package dev.joseluisgs.models

import java.time.LocalDate

class Docente(
    id: Long,
    nombre: String,
    val modulo: String,
    createdAt: LocalDate = LocalDate.now(),
    updatedAt: LocalDate? = null,
    isDeleted: Boolean? = false
) :
    Persona(id, nombre, createdAt, updatedAt, isDeleted) {
    override fun toString(): String {
        return "Docente(id=$id, nombre=$nombre, modulo=$modulo, createdAt=$createdAt, updatedAt=$updatedAt, isDeleted=$isDeleted)"
    }
}
