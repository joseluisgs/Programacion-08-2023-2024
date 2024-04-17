package dev.joseluisgs.repositories.personas

import dev.joseluisgs.models.Persona
import dev.joseluisgs.repositories.crud.CrudRepository

interface PersonasRepository : CrudRepository<Persona, Long> {
    fun findByTipo(tipo: String): List<Persona>
}