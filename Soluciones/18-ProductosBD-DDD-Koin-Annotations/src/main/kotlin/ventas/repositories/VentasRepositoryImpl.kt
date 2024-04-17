package dev.joseluisgs.ventas.repositories

import dev.joseluisgs.database.service.SqlDeLightManager
import dev.joseluisgs.productos.reposiitories.ClientesRepository
import dev.joseluisgs.productos.reposiitories.ProductosRepository
import dev.joseluisgs.ventas.mappers.toLineaVenta
import dev.joseluisgs.ventas.mappers.toVenta
import dev.joseluisgs.ventas.models.Venta
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.util.*

/**
 * Lo ideal sería inyectarle los repositorios de los modelos relacionados
 * para que no tenga que hacer consultas directas a la base de datos
 */

private val logger = logging()

@Singleton
class VentasRepositoryImpl(
    private val dbManager: SqlDeLightManager,
    private val productosRepository: ProductosRepository,
    private val clientesRepository: ClientesRepository,
) : VentasRepository {

    private val db = dbManager.databaseQueries

    /**
     * Obtiene una venta por su id
     */
    override fun findById(id: UUID): Venta? {
        logger.debug { "Obteniendo venta por id: $id" }
        // Existe la venta
        if (db.existsVenta(id.toString()).executeAsOne()) {
            // Obtenemos la venta en bruto
            val ventaEntity = db.selectVentaById(id.toString()).executeAsOne()
            // Obtenemos el cliente
            val cliente = clientesRepository.findById(ventaEntity.cliente_id)!!
            // obtenemos las lineas de venta con los productos
            val lineasVenta = db.selectAllLineasVentaByVentaId(ventaEntity.id).executeAsList()
                .map { it.toLineaVenta(productosRepository.findById(it.producto_id)!!) }
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