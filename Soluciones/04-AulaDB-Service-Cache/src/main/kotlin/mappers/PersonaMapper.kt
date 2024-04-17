package dev.joseluisgs.mappers

import dev.joseluisgs.dto.DocenteDto
import dev.joseluisgs.dto.EstudianteDto
import dev.joseluisgs.dto.PersonaDto
import dev.joseluisgs.models.Docente
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.models.Persona
import java.time.LocalDate

fun PersonaDto.toPersona(): Persona {
    return when (this.tipo) {
        "Estudiante" -> Estudiante(
            id = this.id,
            nombre = this.nombre,
            edad = this.edad!!,
            createdAt = LocalDate.parse(this.createdAt),
            updatedAt = this.updatedAt?.let { LocalDate.parse(it) },
            isDeleted = this.isDeleted
        )

        "Profesor" -> Docente(
            id = this.id,
            nombre = this.nombre,
            modulo = this.modulo!!,
            createdAt = LocalDate.parse(this.createdAt),
            updatedAt = this.updatedAt?.let { LocalDate.parse(it) },
            isDeleted = this.isDeleted
        )

        else -> throw IllegalArgumentException("Tipo de persona no soportado")
    }
}

fun Persona.toPersonaDto(): PersonaDto {
    return when (this) {
        is Estudiante -> PersonaDto(
            id = this.id,
            nombre = this.nombre,
            createdAt = this.createdAt.toString(),
            tipo = "Estudiante",
            modulo = null,
            edad = this.edad,
            updatedAt = this.updatedAt?.toString(),
            isDeleted = this.isDeleted
        )

        is Docente -> PersonaDto(
            id = this.id,
            nombre = this.nombre,
            createdAt = this.createdAt.toString(),
            tipo = "Profesor",
            modulo = this.modulo,
            edad = null,
            updatedAt = this.updatedAt?.toString(),
            isDeleted = this.isDeleted
        )

        else -> throw IllegalArgumentException("Tipo de persona no soportado")
    }
}

fun Docente.toDocenteDto(): DocenteDto {
    return DocenteDto(
        id = this.id,
        nombre = this.nombre,
        modulo = this.modulo,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt?.toString(),
        isDeleted = this.isDeleted
    )
}

fun DocenteDto.toDocente(): Docente {
    return Docente(
        id = this.id,
        nombre = this.nombre,
        modulo = this.modulo,
        createdAt = LocalDate.parse(this.createdAt),
        updatedAt = this.updatedAt?.let { LocalDate.parse(it) },
        isDeleted = this.isDeleted
    )
}

fun Estudiante.toEstudianteDto(): EstudianteDto {
    return EstudianteDto(
        id = this.id,
        nombre = this.nombre,
        edad = this.edad,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt?.toString(),
        isDeleted = this.isDeleted
    )
}

fun EstudianteDto.toEstudiante(): Estudiante {
    return Estudiante(
        id = this.id,
        nombre = this.nombre,
        edad = this.edad,
        createdAt = LocalDate.parse(this.createdAt),
        updatedAt = this.updatedAt?.let { LocalDate.parse(it) },
        isDeleted = this.isDeleted
    )
}
