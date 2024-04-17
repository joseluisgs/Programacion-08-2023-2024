package dev.joseluisgs.repositories.productos

import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.services.database.DataBaseManager
import org.lighthousegames.logging.logging
import java.sql.PreparedStatement
import java.time.LocalDateTime

private val logger = logging()

class ProductosRepositoryImpl : ProductosRepository {

    /**
     * Obtiene todos los productos
     */
    override fun findAll(): List<Producto> {
        logger.debug { "Obteniendo todos los productos" }
        val result = mutableListOf<Producto>() // Lista mutable de productos
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos"
            // Consulta sin parámetros
            val stmt = db.connection?.prepareStatement(sql)!!
            val rs = stmt.executeQuery() // Ejecutamos la consulta
            // Recorremos el resultado
            while (rs.next()) {
                val producto = Producto(
                    id = rs.getLong("id"),
                    nombre = rs.getString("nombre"),
                    precio = rs.getDouble("precio"),
                    stock = rs.getInt("stock"),
                    categoria = Categoria.valueOf(rs.getString("categoria")), // Convertimos el string a enum
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                    isDeleted = rs.getBoolean("is_deleted")
                )
                result.add(producto) // Añadimos el producto a la lista
            }
        }
        return result // Devolvemos la lista
    }


    override fun findById(id: Long): Producto? {
        logger.debug { "Obteniendo producto por id: $id" }
        var result: Producto? = null // Producto a devolver si lo encontramos
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos WHERE id = ?"
            // Consulta con parámetros
            val stmt = db.connection?.prepareStatement(sql)?.apply {
                setLong(1, id) // Asignamos el valor al parámetro
            }!!
            val rs = stmt.executeQuery() // Ejecutamos la consulta
            // Si hay resultado
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
        return result // Devolvemos el producto
    }

    override fun findByCategoria(categoria: String): List<Producto> {
        logger.debug { "Obteniendo productos por categoría: $categoria" }
        val result = mutableListOf<Producto>() // Lista mutable de productos
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos WHERE categoria = ?"
            // Consulta con parámetros
            val stmt = db.connection?.prepareStatement(sql)?.apply {
                setString(1, categoria) // Asignamos el valor al parámetro
            }!!
            val rs = stmt.executeQuery() // Ejecutamos la consulta
            // Recorremos el resultado
            while (rs.next()) {
                val producto = Producto(
                    id = rs.getLong("id"),
                    nombre = rs.getString("nombre"),
                    precio = rs.getDouble("precio"),
                    stock = rs.getInt("stock"),
                    categoria = Categoria.valueOf(rs.getString("categoria")), // Convertimos el string a enum
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
                    isDeleted = rs.getBoolean("is_deleted")
                )
                result.add(producto) // Añadimos el producto a la lista
            }
        }
        return result // Devolvemos la lista
    }

    override fun save(producto: Producto): Producto {
        logger.debug { "Guardando producto: $producto" }
        val timeStamp = LocalDateTime.now() // Fecha actual
        var result: Producto = producto // Producto a devolver
        DataBaseManager.use { db ->
            val sql =
                "INSERT INTO productos (nombre, precio, stock, categoria, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)"
            // Creamos la consulta con los parámetros. Importante Return Generated Keys para obtener el id generado automáticamente
            val stmt =
                db.connection?.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)?.apply {
                    setString(1, producto.nombre)
                    setDouble(2, producto.precio)
                    setInt(3, producto.stock)
                    setString(4, producto.categoria.name)
                    setTimestamp(5, java.sql.Timestamp.valueOf(timeStamp))
                    setTimestamp(6, java.sql.Timestamp.valueOf(timeStamp))
                    setBoolean(7, false)
                }!!
            val rs = stmt.executeUpdate() // Ejecutamos la consulta
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
        return result // Devolvemos el producto
    }

    override fun update(id: Long, producto: Producto): Producto? {
        logger.debug { "Actualizando producto por id: $id" }
        var result: Producto = this.findById(id) ?: return null // Producto a devolver si lo encontramos
        DataBaseManager.use { db ->
            val sql =
                "UPDATE productos SET nombre = ?, precio = ?, stock = ?, categoria = ?, updated_at = ? WHERE id = ?"
            // Creamos la consulta con los parámetros
            val stmt = db.connection?.prepareStatement(sql)?.apply {
                setString(1, producto.nombre)
                setDouble(2, producto.precio)
                setInt(3, producto.stock)
                setString(4, producto.categoria.name)
                setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()))
                setLong(6, id)
            }!!
            val rs = stmt.executeUpdate() // Ejecutamos la consulta
            if (rs > 0) {
                result = producto.copy(
                    id = id,
                    updatedAt = LocalDateTime.now()
                )
            }
        }
        return result // Devolvemos el producto

    }

    override fun delete(id: Long): Producto? {
        logger.debug { "Borrando producto por id: $id" }
        var result: Producto = this.findById(id) ?: return null // Producto a devolver si lo encontramos
        DataBaseManager.use { db ->
            val sql = "DELETE FROM productos WHERE id = ?"
            // Creamos la consulta con los parámetros
            val stmt = db.connection?.prepareStatement(sql)?.apply {
                setLong(1, id)
            }!!
            val rs = stmt.executeUpdate()
            if (rs > 0) {
                result = result.copy(isDeleted = true)
            }
        }
        return result
    }
}