package dev.joseluisgs.services.personas

import dev.joseluisgs.exceptions.personas.PersonasException
import dev.joseluisgs.models.Persona
import dev.joseluisgs.repositories.personas.PersonasRepository
import dev.joseluisgs.services.storage.Storage
import org.lighthousegames.logging.logging


private val logger = logging()

class PersonasServiceImpl(
    private val storageCsv: Storage<Persona>,
    private val storageJson: Storage<Persona>,
    private val personasRepository: PersonasRepository
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
        return personasRepository.findById(id)
            ?: throw PersonasException.PersonaNotFoundException("Persona no encontrada con id $id")
    }

    override fun save(persona: Persona): Persona {
        logger.debug { "Guardando persona $persona" }
        return personasRepository.save(persona)
    }

    override fun update(id: Long, persona: Persona): Persona {
        logger.debug { "Actualizando persona con id $id" }
        return personasRepository.update(id, persona)
            ?: throw PersonasException.PersonaNotFoundException("Persona no encontrada con id $id")
    }

    override fun delete(id: Long, logical: Boolean): Persona {
        logger.debug { "Borrando persona con id $id" }
        return personasRepository.delete(id)
            ?: throw PersonasException.PersonaNotFoundException("Persona no encontrada con id $id")
    }
}
