package dev.joseluisgs.repositories.productos

import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.services.database.DataBaseManager
import org.lighthousegames.logging.logging
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime

private val logger = logging()


class ProductosRepositoryImpl : ProductosRepository {

    /**
     * Convierte un ResultSet a Producto, porque lo necesitamos para trabajar con la base de datos
     */
    private fun ResultSet.toProducto(): Producto {
        return Producto(
            id = getLong("id"),
            nombre = getString("nombre"),
            precio = getDouble("precio"),
            stock = getInt("stock"),
            categoria = Categoria.valueOf(getString("categoria")),
            createdAt = getTimestamp("created_at").toLocalDateTime(),
            updatedAt = getTimestamp("updated_at").toLocalDateTime(),
            isDeleted = getBoolean("is_deleted")
        )
    }

    /**
     * Obtiene todos los productos de la base de datos
     */
    override fun findAll(): List<Producto> {
        logger.debug { "Obteniendo todos los productos" }
        val result = mutableListOf<Producto>()
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos"
            val stmt = db.prepareStatement(sql) // Preparar la consulta
            val rs = stmt.executeQuery() // Ejecutar la consulta
            while (rs.next()) {
                result.add(rs.toProducto()) // Convertir ResultSet a Producto y agregarlo a la lista
            }
        }
        return result
    }

    /**
     * Obtiene un producto por id
     */
    override fun findById(id: Long): Producto? {
        logger.debug { "Obteniendo producto por id: $id" }
        var result: Producto? = null
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos WHERE id = ?"
            // Preparar la consulta con el id del producto
            val stmt = db.prepareStatement(sql).apply {
                setLong(1, id)
            }
            val rs = stmt.executeQuery() // Ejecutar la consulta
            if (rs.next()) {
                result = rs.toProducto() // Convertir ResultSet a Producto
            }
        }
        return result
    }

    /**
     * Obtiene productos por categoría
     */
    override fun findByCategoria(categoria: String): List<Producto> {
        logger.debug { "Obteniendo productos por categoría: $categoria" }
        val result = mutableListOf<Producto>()
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos WHERE categoria = ?"
            // Preparar la consulta con parametro de categoría
            val stmt = db.prepareStatement(sql).apply {
                setString(1, categoria)
            }
            val rs = stmt.executeQuery()
            while (rs.next()) {
                result.add(rs.toProducto()) // Convertir ResultSet a Producto y agregarlo a la lista
            }
        }
        return result
    }

    /**
     * Guarda un producto en la base de datos
     */
    override fun save(producto: Producto): Producto {
        logger.debug { "Guardando producto: $producto" }
        val timeStamp = LocalDateTime.now()
        var result: Producto = producto
        DataBaseManager.use { db ->
            val sql =
                "INSERT INTO productos (nombre, precio, stock, categoria, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)"
            // Preparar la consulta con los datos del producto  y obtener la clave generada
            val stmt = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS).apply {
                setString(1, producto.nombre)
                setDouble(2, producto.precio)
                setInt(3, producto.stock)
                setString(4, producto.categoria.name)
                setTimestamp(5, Timestamp.valueOf(timeStamp))
                setTimestamp(6, Timestamp.valueOf(timeStamp))
                setBoolean(7, false)
            }
            val rs = stmt.executeUpdate() // Ejecutar la consulta
            // Si se ha insertado correctamente obtenemos la clave generada
            if (rs > 0) {
                val generatedKeys = stmt.generatedKeys
                if (generatedKeys.next()) {
                    result = producto.copy(
                        id = generatedKeys.getLong(1),
                        createdAt = timeStamp,
                        updatedAt = timeStamp
                    )
                }
            }
        }
        return result
    }

    /**
     * Actualiza un producto en la base de datos
     */
    override fun update(id: Long, producto: Producto): Producto? {
        logger.debug { "Actualizando producto por id: $id" }
        var result: Producto = this.findById(id) ?: return null
        DataBaseManager.use { db ->
            val sql =
                "UPDATE productos SET nombre = ?, precio = ?, stock = ?, categoria = ?, updated_at = ? WHERE id = ?"
            // Creamos la consulta con los parámetros
            val stmt = db.prepareStatement(sql).apply {
                setString(1, producto.nombre)
                setDouble(2, producto.precio)
                setInt(3, producto.stock)
                setString(4, producto.categoria.name)
                setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()))
                setLong(6, id)
            }
            val rs = stmt.executeUpdate() // Ejecutar la consulta
            // Si se ha actualizado correctamente actualizamos el producto
            if (rs > 0) {
                result = producto.copy(
                    id = id,
                    updatedAt = LocalDateTime.now()
                )
            }
        }
        return result
    }

    /**
     * Borra un producto de la base de datos
     */
    override fun delete(id: Long): Producto? {
        logger.debug { "Borrando producto por id: $id" }
        var result: Producto = this.findById(id) ?: return null
        DataBaseManager.use { db ->
            val sql = "DELETE FROM productos WHERE id = ?"
            // Preparar la consulta con el id del producto
            val stmt = db.prepareStatement(sql).apply {
                setLong(1, id)
            }
            val rs = stmt.executeUpdate() // Ejecutar la consulta
            // Si se ha borrado correctamente actualizamos el producto con isDeleted a true
            if (rs > 0) {
                result = result.copy(isDeleted = true)
            }
        }
        return result
    }
}
