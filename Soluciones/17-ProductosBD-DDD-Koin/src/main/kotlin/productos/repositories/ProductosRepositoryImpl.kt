package dev.joseluisgs.productos.repositories

import dev.joseluisgs.database.service.SqlDeLightManager
import dev.joseluisgs.productos.mappers.toProducto
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.reposiitories.ProductosRepository
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

private val logger = logging()

class ProductosRepositoryImpl(
    private val dbManager: SqlDeLightManager
) : ProductosRepository {

    private val db = dbManager.databaseQueries

    /**
     * Obtiene todos los productos de la base de datos
     */
    override fun findAll(): List<Producto> {
        logger.debug { "Obteniendo todos los productos" }
        return db.selectAllProductos()
            .executeAsList()
            .map { it.toProducto() }
    }

    /**
     * Obtiene un producto por id
     */
    override fun findById(id: Long): Producto? {
        logger.debug { "Obteniendo producto por id: $id" }
        return db.selectProductoById(id)
            .executeAsOneOrNull()
            ?.toProducto()
    }

    /**
     * Obtiene productos por categoría
     */
    override fun findByCategoria(categoria: String): List<Producto> {
        logger.debug { "Obteniendo productos por categoría: $categoria" }
        return db.selectAllProductosByCategoria(categoria)
            .executeAsList()
            .map { it.toProducto() }
    }

    /**
     * Guarda un producto en la base de datos
     */
    override fun save(producto: Producto): Producto {
        logger.debug { "Guardando producto: $producto" }

        val timeStamp = LocalDateTime.now().toString()

        // Las transacciones son necesarias para que se hagan todas las operaciones juntas
        // Si no se hace así, se pueden hacer operaciones intermedias y no se garantiza la atomicidad
        // esto es así porque insertamos y luego seleccionamos el último insertado para el id
        db.transaction {
            db.insertProducto(
                nombre = producto.nombre,
                precio = producto.precio,
                stock = producto.stock.toLong(),
                categoria = producto.categoria.name,
                created_at = timeStamp,
                updated_at = timeStamp
            )
        }
        return db.selectProductoLastInserted()
            .executeAsOne()
            .toProducto()
    }

    /**
     * Actualiza un producto en la base de datos
     */
    override fun update(id: Long, producto: Producto): Producto? {
        logger.debug { "Actualizando producto por id: $id" }
        var result = this.findById(id) ?: return null
        val timeStamp = LocalDateTime.now()

        result = result.copy(
            nombre = producto.nombre,
            precio = producto.precio,
            stock = producto.stock,
            categoria = producto.categoria,
            isDeleted = producto.isDeleted,
            updatedAt = timeStamp
        )

        db.updateProducto(
            nombre = result.nombre,
            precio = result.precio,
            stock = result.stock.toLong(),
            categoria = result.categoria.name,
            is_deleted = if (result.isDeleted) 1 else 0,
            updated_at = timeStamp.toString(),
            id = producto.id,
        )
        return result
    }

    /**
     * Borra un producto de la base de datos
     */
    override fun delete(id: Long): Producto? {
        logger.debug { "Borrando producto por id: $id" }
        val result = this.findById(id) ?: return null
        // Esto es borrado fisico, pero lo ideal sería un borrado lógico si se quiere mantener la integridad
        // db.deleteProducto(id)
        // Borrado lógico
        val timeStamp = LocalDateTime.now()
        db.updateProducto(
            nombre = result.nombre,
            precio = result.precio,
            stock = result.stock.toLong(),
            categoria = result.categoria.name,
            is_deleted = 1,
            updated_at = timeStamp.toString(),
            id = result.id,
        )
        return result.copy(isDeleted = true, updatedAt = timeStamp)
    }
}