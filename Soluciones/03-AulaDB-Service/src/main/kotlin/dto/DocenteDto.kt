package dev.joseluisgs.dto

data class DocenteDto(
    val id: Long,
    val nombre: String,
    val modulo: String,
    val createdAt: String,
    val updatedAt: String?,
    val isDeleted: Boolean?
)