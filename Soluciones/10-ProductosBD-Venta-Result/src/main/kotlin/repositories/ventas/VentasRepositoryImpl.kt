package dev.joseluisgs.repositories.ventas

import dev.joseluisgs.models.*
import dev.joseluisgs.services.database.DataBaseManager
import org.lighthousegames.logging.logging
import java.sql.ResultSet
import java.util.*


private val logger = logging()

/**
 * Lo ideal sería inyectarle los repositorios de los modelos relacionados
 * para que no tenga que hacer consultas directas a la base de datos
 */

class VentasRepositoryImpl : VentasRepository {

    /**
     * Obtiene una venta por su id
     */
    override fun findById(id: UUID): Venta? {
        logger.debug { "Obteniendo venta por id: $id" }
        var result: Venta? = null
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM ventas WHERE id = ?"
            val stmt = db.prepareStatement(sql).apply {
                setObject(1, id)
            }
            val rs = stmt.executeQuery()
            if (rs.next()) {
                logger.debug { "Venta encontrada: ${rs.getObject("id", UUID::class.java)}" }
                result = rs.toVenta()
            }
        }
        return result
    }

    /**
     * Guarda una venta
     */
    override fun save(venta: Venta): Venta {
        logger.debug { "Guardando venta: $venta" }
        // Guardamos la venta
        DataBaseManager.use { db ->
            val sql = "INSERT INTO ventas (id, cliente_id, total) VALUES (?, ?, ?)"
            val stmt = db.prepareStatement(sql).apply {
                setObject(1, venta.id)
                setLong(2, venta.cliente.id)
                setDouble(3, venta.total)
            }
            stmt.executeUpdate()
        }
        // Luego guardamos las lineas de venta porque necesitan su id de venta
        venta.lineas.forEach {
            DataBaseManager.use { db ->
                val sql =
                    "INSERT INTO lineas_ventas (id, venta_id, producto_id, cantidad, precio) VALUES (?, ?, ?, ?, ?)"
                val stmt = db.prepareStatement(sql).apply {
                    setObject(1, it.id)
                    setObject(2, venta.id)
                    setLong(3, it.producto.id)
                    setInt(4, it.cantidad)
                    setDouble(5, it.precio)
                }
                stmt.executeUpdate()
            }
        }

        return venta
    }

    /**
     * Convierte un ResultSet a una venta (Mapeo)
     */
    private fun ResultSet.toVenta(): Venta {
        return Venta(
            id = getObject("id", UUID::class.java),
            cliente = buscarCliente(getLong("cliente_id")), // TODO: Implementar
            lineas = buscarLineas(UUID.fromString(getString("id"))),
            createdAt = getTimestamp("created_at").toLocalDateTime(),
            updatedAt = getTimestamp("updated_at").toLocalDateTime()
        )
    }

    /**
     * obtiene un cliente por su id
     */
    fun buscarCliente(id: Long): Cliente {
        logger.debug { "Buscando cliente por id: $id" }
        lateinit var result: Cliente
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM clientes WHERE id = ?"
            val stmt = db.prepareStatement(sql).apply {
                setLong(1, id)
            }
            val rs = stmt.executeQuery()
            if (rs.next()) {
                result = Cliente(
                    id = rs.getLong("id"),
                    nombre = rs.getString("nombre"),
                    email = rs.getString("email"),
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                    isDeleted = rs.getBoolean("is_deleted")
                )
            }
        }
        return result
    }

    /**
     * Obtiene las lineas de venta de una venta
     */
    private fun buscarLineas(ventaId: UUID): List<LineaVenta> {
        logger.debug { "Buscando lineas de venta por id: $ventaId" }
        val result = mutableListOf<LineaVenta>()
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM lineas_ventas WHERE venta_id = ?"
            val stmt = db.prepareStatement(sql).apply {
                setObject(1, ventaId)
            }
            val rs = stmt.executeQuery()
            while (rs.next()) {
                result.add(
                    LineaVenta(
                        id = rs.getObject("id", UUID::class.java),
                        producto = buscarProducto(rs.getLong("producto_id")),
                        cantidad = rs.getInt("cantidad"),
                        precio = rs.getDouble("precio"),
                        createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                        updatedAt = rs.getTimestamp("updated_at").toLocalDateTime()
                    )
                )
            }
        }
        return result
    }

    /**
     * Obtiene un producto por su id
     */
    private fun buscarProducto(id: Long): Producto {
        logger.debug { "Buscando producto por id: $id" }
        lateinit var result: Producto
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos WHERE id = ?"
            val stmt = db.prepareStatement(sql).apply {
                setObject(1, id)
            }
            val rs = stmt.executeQuery()
            if (rs.next()) {
                result = Producto(
                    id = rs.getLong("id"),
                    nombre = rs.getString("nombre"),
                    precio = rs.getDouble("precio"),
                    stock = rs.getInt("stock"),
                    categoria = Categoria.valueOf(rs.getString("categoria")),
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                    isDeleted = rs.getBoolean("is_deleted")
                )
            }
        }
        return result
    }

}