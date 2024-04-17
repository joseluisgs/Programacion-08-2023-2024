package dev.joseluisgs.dto

data class EstudianteDto(
    val id: Long,
    val nombre: String,
    val edad: Int,
    val createdAt: String,
    val updatedAt: String?,
    val isDeleted: Boolean?
)