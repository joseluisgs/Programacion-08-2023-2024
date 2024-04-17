package dev.joseluisgs.repositories.personas

import dev.joseluisgs.dto.PersonaDto
import dev.joseluisgs.exceptions.personas.PersonasException
import dev.joseluisgs.mappers.toPersona
import dev.joseluisgs.mappers.toPersonaDto
import dev.joseluisgs.models.Persona
import dev.joseluisgs.services.database.DataBaseManager
import org.lighthousegames.logging.logging
import java.sql.Statement.RETURN_GENERATED_KEYS
import java.time.LocalDate

private val logger = logging()

class PersonasRepositoryImpl : PersonasRepository {

    override fun findAll(): List<Persona> {
        logger.debug { "Buscando todas las personas" }
        try {
            val personas = mutableListOf<Persona>()
            DataBaseManager.use { db ->
                val sql = "SELECT * FROM personas"
                val result = db.connection?.prepareStatement(sql)!!.executeQuery()
                while (result.next()) {
                    val persona = PersonaDto(
                        id = result.getLong("id"),
                        nombre = result.getString("nombre"),
                        createdAt = result.getString("created_at"),
                        tipo = result.getString("tipo"),
                        modulo = result.getString("modulo"),
                        edad = result.getInt("edad"),
                        updatedAt = result.getString("updated_at"),
                        isDeleted = result.getBoolean("is_deleted")
                    ).toPersona()
                    personas.add(persona)
                }
            }
            return personas
        } catch (e: Exception) {
            logger.error { "Error al buscar todas las personas" }
            throw PersonasException.PersonasNotFetchedException("Error al buscar todas las personas")
        }
    }

    override fun findByTipo(tipo: String): List<Persona> {
        logger.debug { "Buscando personas por tipo $tipo" }
        try {
            val personas = mutableListOf<Persona>()
            DataBaseManager.use { db ->
                val sql = "SELECT * FROM personas WHERE tipo = ?"
                val statement = db.connection?.prepareStatement(sql)!!
                statement.setString(1, tipo)
                val result = statement.executeQuery()
                while (result.next()) {
                    val persona = PersonaDto(
                        id = result.getLong("id"),
                        nombre = result.getString("nombre"),
                        createdAt = result.getString("created_at"),
                        tipo = result.getString("tipo"),
                        modulo = result.getString("modulo"),
                        edad = result.getInt("edad"),
                        updatedAt = result.getString("updated_at"),
                        isDeleted = result.getBoolean("is_deleted")
                    ).toPersona()
                    personas.add(persona)
                }
            }
            return personas
        } catch (e: Exception) {
            logger.error { "Error al buscar personas por tipo $tipo" }
            throw PersonasException.PersonasNotFetchedException("Error al buscar personas por tipo $tipo")
        }
    }

    override fun findById(id: Long): Persona? {
        logger.debug { "Buscando persona por id $id" }
        try {
            var persona: Persona? = null
            DataBaseManager.use { db ->
                val sql = "SELECT * FROM personas WHERE id = ?"
                val statement = db.connection?.prepareStatement(sql)!!
                statement.setLong(1, id)
                val result = statement.executeQuery()
                if (result.next()) {
                    persona = PersonaDto(
                        id = result.getLong("id"),
                        nombre = result.getString("nombre"),
                        createdAt = result.getString("created_at"),
                        tipo = result.getString("tipo"),
                        modulo = result.getString("modulo"),
                        edad = result.getInt("edad"),
                        updatedAt = result.getString("updated_at"),
                        isDeleted = result.getBoolean("is_deleted")
                    ).toPersona()
                }
            }
            return persona
        } catch (e: Exception) {
            logger.error { "Error al buscar persona por id $id" }
            throw PersonasException.PersonasNotFetchedException("Error al buscar persona por id $id")
        }
    }

    override fun save(item: Persona): Persona {
        logger.debug { "Guardando persona $item" }
        try {
            var persona: PersonaDto = item.toPersonaDto()
            val timeStamp = LocalDate.now()
            DataBaseManager.use { db ->
                val sql =
                    "INSERT INTO personas (nombre, tipo, modulo, edad, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)"
                val statement = db.connection?.prepareStatement(sql, RETURN_GENERATED_KEYS)!!
                statement.setString(1, persona.nombre)
                statement.setString(2, persona.tipo)
                statement.setString(3, persona.modulo)
                statement.setObject(4, persona.edad) // Para que acepte null ponemos Object
                statement.setString(5, timeStamp.toString())
                statement.setString(6, timeStamp.toString())
                statement.setBoolean(7, false)
                statement.executeUpdate()
                // Si queremos recuperar el ID generado
                val id = statement.generatedKeys.getLong(1)
                persona = persona.copy(
                    id = id,
                    createdAt = timeStamp.toString(),
                    updatedAt = timeStamp.toString(),
                    isDeleted = false
                )
            }
            return persona.toPersona()
        } catch (e: Exception) {
            logger.error { "Error al guardar persona $item" }
            throw PersonasException.PersonaNotSavedException("Error al guardar persona $item")
        }
    }

    override fun update(id: Long, item: Persona): Persona? {
        logger.debug { "Actualizando persona $item" }
        try {
            var persona: PersonaDto? = this.findById(id)?.toPersonaDto()
            if (persona != null) {
                persona = item.toPersonaDto()
                val timeStamp = LocalDate.now()
                DataBaseManager.use { db ->
                    val sql =
                        "UPDATE personas SET nombre = ?, tipo = ?, modulo = ?, edad = ?, updated_at = ? WHERE id = ?"
                    val statement = db.connection?.prepareStatement(sql)!!
                    statement.setString(1, persona?.nombre)
                    statement.setString(2, persona?.tipo)
                    statement.setString(3, persona?.modulo)
                    statement.setObject(4, persona?.edad) // Para que acepte null ponemos Object
                    statement.setString(5, timeStamp.toString())
                    statement.setLong(6, id)
                    statement.executeUpdate()
                    persona = persona?.copy(
                        updatedAt = timeStamp.toString()
                    )
                }
            }
            return persona?.toPersona()
        } catch (e: Exception) {
            logger.error { "Error al actualizar persona $item" }
            throw PersonasException.PersonaNotUpdatedException("Error al actualizar persona $item")
        }
    }

    override fun delete(id: Long, logical: Boolean): Persona? {
        logger.debug { "Borrando persona con id $id" }
        try {
            var persona: PersonaDto? = this.findById(id)?.toPersonaDto()
            if (persona != null) {
                DataBaseManager.use { db ->
                    // Borramos logico
                    if (logical) {
                        val sql = "UPDATE personas SET is_deleted = ? WHERE id = ?"
                        val statement = db.connection?.prepareStatement(sql)!!
                        statement.setBoolean(1, true)
                        statement.setLong(2, id)
                        statement.executeUpdate()
                        persona = persona?.copy(
                            isDeleted = true
                        )
                    } else {
                        // Borramos fisico
                        val sql = "DELETE FROM personas WHERE id = ?"
                        val statement = db.connection?.prepareStatement(sql)!!
                        statement.setLong(1, id)
                        statement.executeUpdate()
                    }
                }
            }
            return persona?.toPersona()
        } catch (e: Exception) {
            logger.error { "Error al borrar persona con id $id" }
            throw PersonasException.PersonaNotDeletedException("Error al borrar persona con id $id")
        }
    }

}