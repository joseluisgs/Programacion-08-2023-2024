package dev.joseluisgs.services.personas

import dev.joseluisgs.models.Persona

interface PersonasService {
    fun loadFromCsv(): List<Persona>
    fun storeToCsv(personas: List<Persona>)
    fun loadFromJson(): List<Persona>
    fun storeToJson(personas: List<Persona>)
    fun findAll(): List<Persona>
    fun findByTipo(tipo: String): List<Persona>
    fun findById(id: Long): Persona
    fun save(persona: Persona): Persona
    fun update(id: Long, persona: Persona): Persona
    fun delete(id: Long, logical: Boolean = false): Persona
}