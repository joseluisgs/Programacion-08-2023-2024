package dev.joseluisgs.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonaDto(
    val id: Long,
    val nombre: String,
    @SerialName("created_at")
    val createdAt: String,
    val tipo: String,
    val modulo: String?,
    val edad: Int?,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("is_deleted")
    val isDeleted: Boolean?
)
