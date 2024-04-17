package dev.joseluisgs.services.storage

import dev.joseluisgs.config.Config
import dev.joseluisgs.dto.PersonaDto
import dev.joseluisgs.exceptions.storage.StorageException
import dev.joseluisgs.mappers.toPersona
import dev.joseluisgs.mappers.toPersonaDto
import dev.joseluisgs.models.Persona
import org.lighthousegames.logging.logging
import kotlin.io.path.Path

private val logger = logging()

class StoragePersonasCsv : Storage<Persona> {
    override fun store(data: List<Persona>): Boolean {
        logger.debug { "Guardando personas en fichero csv" }
        try {
            val file = Path(Config.storageData, "personas-back.csv").toFile()
            file.writeText("id,nombre,created_at,tipo,modulo,edad\n")
            data.map { it.toPersonaDto() }
                .forEach {
                    file.appendText("${it.id},${it.nombre},${it.createdAt},${it.tipo},${it.modulo ?: ""},${it.edad ?: ""}\n")
                }
            logger.debug { "Guardado correctamente" }
            return true
        } catch (e: Exception) {
            logger.error { "Error al guardar el fichero csv de personas: ${e.message}" }
            throw StorageException.StoreException("Error al guardar el fichero csv de personas: ${e.message}")
        }
    }

    override fun load(fileName: String): List<Persona> {
        try {
            logger.debug { "Cargando personas desde fichero csv" }
            val file = Path(Config.storageData, fileName).toFile()
            if (!file.exists()) throw StorageException.LoadException("El fichero $fileName no existe")
            return file.readLines().drop(1)
                .map {
                    val data = it.split(",")
                    PersonaDto(
                        id = data[0].toLong(),
                        nombre = data[1],
                        createdAt = data[2],
                        tipo = data[3],
                        modulo = data[4],
                        edad = data[5].toIntOrNull(),
                        updatedAt = null,
                        isDeleted = null
                    ).toPersona()
                }
        } catch (e: Exception) {
            logger.error { "Error al cargar el fichero csv de personas: ${e.message}" }
            throw StorageException.LoadException("Error al cargar el fichero csv de personas: ${e.message}")
        }
    }
}