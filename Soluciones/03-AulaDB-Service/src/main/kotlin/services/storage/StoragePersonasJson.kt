package dev.joseluisgs.services.storage

import dev.joseluisgs.config.Config
import dev.joseluisgs.dto.PersonaDto
import dev.joseluisgs.exceptions.storage.StorageException
import dev.joseluisgs.mappers.toPersona
import dev.joseluisgs.mappers.toPersonaDto
import dev.joseluisgs.models.Persona
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.logging
import kotlin.io.path.Path

private val logger = logging()

class StoragePersonasJson : Storage<Persona> {
    @OptIn(ExperimentalSerializationApi::class)
    override fun store(data: List<Persona>): Boolean {
        logger.debug { "Guardando personas en fichero json" }
        try {
            val file = Path(Config.storageData, "personas-back.json").toFile()
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
            // Cuidado que necesito un DTO
            file.writeText(json.encodeToString<List<PersonaDto>>(data.map { it.toPersonaDto() }))
            logger.debug { "Guardado correctamente" }
            return true
        } catch (e: Exception) {
            logger.error { "Error al guardar el fichero json de personas: ${e.message}" }
            throw StorageException.StoreException("Error al guardar el fichero json de personas: ${e.message}")
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun load(fileName: String): List<Persona> {
        try {
            logger.debug { "Cargando personas desde fichero json" }
            val file = Path(Config.storageData, fileName).toFile()
            if (!file.exists()) throw StorageException.LoadException("El fichero $fileName no existe")
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
            return json.decodeFromString<List<PersonaDto>>(file.readText())
                .map { it.toPersona() }

        } catch (e: Exception) {
            logger.error { "Error al cargar el fichero json de personas: ${e.message}" }
            throw StorageException.LoadException("Error al cargar el fichero json de personas: ${e.message}")
        }
    }
}