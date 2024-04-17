package dev.joseluisgs.productos.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.productos.dto.ProductoDto
import dev.joseluisgs.productos.errors.ProductoError
import dev.joseluisgs.productos.mappers.toProducto
import dev.joseluisgs.productos.model.Producto
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.io.File

private val logger = logging()

@Singleton
class ProductosStorageImpl : ProductosStorage {
    override fun load(file: File): Result<List<Producto>, ProductoError> {
        logger.debug { "Cargando productos desde fichero: ${file.absolutePath}" }
        return try {
            Ok(file.readLines().drop(1).map { line ->
                val parts = line.split(",")
                ProductoDto(
                    nombre = parts[0],
                    precio = parts[1].toDouble(),
                    stock = parts[2].toInt(),
                    categoria = parts[3],
                ).toProducto()
            })
        } catch (e: Exception) {
            logger.error { "Error al cargar productos desde fichero: ${file.absolutePath}. ${e.message}" }
            Err(ProductoError.ProductoStorageError("Error al cargar productos desde fichero: ${file.absolutePath}. ${e.message}"))
        }
    }

}