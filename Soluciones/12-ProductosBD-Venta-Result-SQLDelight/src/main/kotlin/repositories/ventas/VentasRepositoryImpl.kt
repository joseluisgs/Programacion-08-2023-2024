package dev.joseluisgs.repositories.ventas

import dev.joseluisgs.mappers.toCliente
import dev.joseluisgs.mappers.toLineaVenta
import dev.joseluisgs.mappers.toProducto
import dev.joseluisgs.mappers.toVenta
import dev.joseluisgs.models.Venta
import dev.joseluisgs.services.database.SqlDeLightClient
import org.lighthousegames.logging.logging
import java.util.*


private val logger = logging()

/**
 * Lo ideal sería inyectarle los repositorios de los modelos relacionados
 * para que no tenga que hacer consultas directas a la base de datos
 */

class VentasRepositoryImpl : VentasRepository {

    private val db = SqlDeLightClient.databaseQueries

    /**
     * Obtiene una venta por su id
     */
    override fun findById(id: UUID): Venta? {
        logger.debug { "Obteniendo venta por id: $id" }
        // Existe la venta
        if (db.existsVenta(id.toString()).executeAsOne() == 1L) {
            // Obtenemos la venta en bruto
            val ventaEntity = db.selectVentaById(id.toString()).executeAsOne()
            // Obtenemos el cliente
            val cliente = db.selectClienteById(ventaEntity.cliente_id).executeAsOne().toCliente()
            // obtenemos las lineas de venta con los productos
            val lineasVenta = db.selectAllLineasVentaByVentaId(ventaEntity.id).executeAsList()
                .map { it.toLineaVenta(db.selectProductoById(it.producto_id).executeAsOne().toProducto()) }
            // Mapeamos la venta
            return ventaEntity.toVenta(cliente, lineasVenta)
        }
        return null
    }

    /**
     * Guarda una venta
     */
    override fun save(venta: Venta): Venta {
        logger.debug { "Guardando venta: $venta" }
        // Guardamos la venta
        db.transaction {
            db.insertVenta(
                id = venta.id.toString(),
                cliente_id = venta.cliente.id,
                total = venta.total,
                created_at = venta.createdAt.toString(),
                updated_at = venta.updatedAt.toString()
            )
        }
        // Guardamos las lineas de venta
        venta.lineas.forEach {
            db.transaction {
                db.insertLineaVenta(
                    id = it.id.toString(),
                    venta_id = venta.id.toString(),
                    producto_id = it.producto.id,
                    cantidad = it.cantidad.toLong(),
                    precio = it.precio,
                    created_at = it.createdAt.toString(),
                    updated_at = it.updatedAt.toString()
                )
            }
        }
        return venta
    }
}