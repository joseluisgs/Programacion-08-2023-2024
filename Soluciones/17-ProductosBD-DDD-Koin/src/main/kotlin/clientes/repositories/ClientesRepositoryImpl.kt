package dev.joseluisgs.clientes.repositories

import dev.joseluisgs.clientes.mappers.toCliente
import dev.joseluisgs.clientes.models.Cliente
import dev.joseluisgs.database.service.SqlDeLightManager
import dev.joseluisgs.productos.reposiitories.ClientesRepository
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

private val logger = logging()

class ClientesRepositoryImpl(
    private val dbManager: SqlDeLightManager
) : ClientesRepository {
    private val db = dbManager.databaseQueries

    override fun findAll(): List<Cliente> {
        logger.debug { "Buscando todos los clientes" }
        return db.selectAllClientes().executeAsList().map { it.toCliente() }
    }

    override fun findById(id: Long): Cliente? {
        logger.debug { "Buscando cliente por id: $id" }
        return db.selectClienteById(id).executeAsOneOrNull()?.toCliente()
    }

    override fun save(cliente: Cliente): Cliente {
        logger.debug { "Guardando cliente: $cliente" }

        val timeStamp = LocalDateTime.now().toString()

        db.transaction {
            db.insertCliente(
                nombre = cliente.nombre,
                email = cliente.email,
                created_at = timeStamp,
                updated_at = timeStamp,
            )
        }

        return db.selectClienteLastInserted().executeAsOne().toCliente()
    }

    override fun update(id: Long, cliente: Cliente): Cliente? {
        logger.debug { "Actualizando cliente por id: $id" }
        var result = this.findById(id) ?: return null
        val timeStamp = LocalDateTime.now()
        result = result.copy(
            nombre = cliente.nombre,
            email = cliente.email,
            isDeleted = cliente.isDeleted,
            updatedAt = timeStamp
        )

        db.updateCliente(
            nombre = result.nombre,
            email = result.email,
            updated_at = timeStamp.toString(),
            is_deleted = if (result.isDeleted) 1 else 0,
            id = id,
        )
        return result
    }

    override fun delete(id: Long): Cliente? {
        logger.debug { "Borrando cliente por id: $id" }
        val result = this.findById(id) ?: return null
        // Esto es borrado lógico
        val timeStamp = LocalDateTime.now()
        db.updateCliente(
            nombre = result.nombre,
            email = result.email,
            is_deleted = 1,
            updated_at = timeStamp.toString(),
            id = result.id,
        )
        return result.copy(isDeleted = true, updatedAt = timeStamp)
    }

}