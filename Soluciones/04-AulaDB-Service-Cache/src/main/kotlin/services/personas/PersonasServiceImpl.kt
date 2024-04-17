package dev.joseluisgs.services.personas

import dev.joseluisgs.exceptions.personas.PersonasException
import dev.joseluisgs.models.Persona
import dev.joseluisgs.repositories.personas.PersonasRepository
import dev.joseluisgs.services.cache.personas.PersonasCache
import dev.joseluisgs.services.storage.Storage
import org.lighthousegames.logging.logging


private val logger = logging()

class PersonasServiceImpl(
    private val storageCsv: Storage<Persona>,
    private val storageJson: Storage<Persona>,
    private val personasRepository: PersonasRepository,
    private val personasCache: PersonasCache
) : PersonasService {
    override fun loadFromCsv(): List<Persona> {
        logger.debug { "Cargando personas desde CSV" }
        return storageCsv.load("personas.csv")
    }

    override fun storeToCsv(personas: List<Persona>) {
        logger.debug { "Guardando personas en CSV" }
        storageCsv.store(personas)
    }

    override fun loadFromJson(): List<Persona> {
        logger.debug { "Cargando personas desde JSON" }
        return storageJson.load("personas-back.json")
    }

    override fun storeToJson(personas: List<Persona>) {
        logger.debug { "Guardando personas en JSON" }
        storageJson.store(personas)
    }

    override fun findAll(): List<Persona> {
        logger.debug { "Buscando todas las personas" }
        return personasRepository.findAll()
    }

    override fun findByTipo(tipo: String): List<Persona> {
        logger.debug { "Buscando personas por tipo $tipo" }
        return personasRepository.findByTipo(tipo)
    }

    override fun findById(id: Long): Persona {
        logger.debug { "Buscando persona por id $id" }
        // Buscamos en cache y si no está, en la base de datos y lo guardamos en cache si no está
        return personasCache.get(id) ?: personasRepository.findById(id)?.also {
            personasCache.put(id, it)
        } ?: throw PersonasException.PersonaNotFoundException("Persona no encontrada con id $id")

    }

    override fun save(persona: Persona): Persona {
        logger.debug { "Guardando persona $persona" }
        return personasRepository.save(persona).also {
            personasCache.put(persona.id, it)
        }
    }

    override fun update(id: Long, persona: Persona): Persona {
        logger.debug { "Actualizando persona con id $id" }
        // Actualizamos en cache y en la base de datos
        return personasRepository.update(id, persona).also {
            personasCache.put(id, it!!)
        } ?: throw PersonasException.PersonaNotFoundException("Persona no encontrada con id $id")
    }

    override fun delete(id: Long, logical: Boolean): Persona {
        logger.debug { "Borrando persona con id $id" }
        return personasRepository.delete(id).also {
            personasCache.remove(id)
        } ?: throw PersonasException.PersonaNotFoundException("Persona no encontrada con id $id")
    }
}
