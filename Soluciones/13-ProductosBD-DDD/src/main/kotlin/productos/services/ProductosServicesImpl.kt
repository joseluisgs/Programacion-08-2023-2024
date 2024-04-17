package dev.joseluisgs.productos.services

import com.github.michaelbull.result.*
import dev.joseluisgs.productos.cache.ProductosCache
import dev.joseluisgs.productos.errors.ProductoError
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.reposiitories.ProductosRepository
import dev.joseluisgs.productos.storage.ProductosStorage
import dev.joseluisgs.productos.validators.ProductoValidator
import org.lighthousegames.logging.logging
import java.io.File

private val logger = logging()

class ProductosServicesImpl(
    private val productosRepository: ProductosRepository,
    private val productoValidator: ProductoValidator,
    private val productosCache: ProductosCache,
    private val productosStorage: ProductosStorage
) : ProductosService {
    override fun getAll(): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo todos los productos" }
        return Ok(productosRepository.findAll())
    }

    override fun getByCategoria(categoria: String): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo productos por categoria: $categoria" }
        return Ok(productosRepository.findByCategoria(categoria))
    }

    override fun getById(id: Long): Result<Producto, ProductoError> {
        logger.debug { "Obteniendo producto por id: $id" }

        /*return productosCache.get(id).onFailure {
            logger.debug { "Producto no encontrado en cache" }
            productosRepository.findById(id)
                ?.let { Ok(it) }
                ?: Err(ProductoError.ProductoNoEncontrado("Producto no encontrado con id: $id"))
        }.onSuccess {
            logger.debug { "Producto encontrado en cache" }
            return Ok(it)
        }.mapError { ProductoError.ProductoNoEncontrado("Producto no encontrado con id: $id") }*/

        return productosCache.get(id).mapBoth(
            success = {
                logger.debug { "Producto encontrado en cache" }
                Ok(it)
            },
            failure = {
                logger.debug { "Producto no encontrado en cache" }
                productosRepository.findById(id)
                    ?.let { Ok(it) }
                    ?: Err(ProductoError.ProductoNoEncontrado("Producto no encontrado con id: $id"))
            }
        )
    }

    override fun create(producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Guardando producto: $producto" }
        return productoValidator.validate(producto).andThen {
            Ok(productosRepository.save(it))
        }.andThen { p ->
            println("Guardando en cache")
            productosCache.put(p.id, p)
        }
    }

    override fun update(id: Long, producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Actualizando producto por id: $id" }
        return productoValidator.validate(producto).andThen { p ->
            productosRepository.update(id, p)
                ?.let { Ok(it) }
                ?: Err(ProductoError.ProductoNoActualizado("Producto no actualizado con id: $id"))
        }.andThen {
            productosCache.put(id, it)
        }
    }

    override fun delete(id: Long): Result<Producto, ProductoError> {
        logger.debug { "Borrando producto por id: $id" }
        return productosRepository.delete(id)
            ?.let {
                productosCache.remove(id)
                Ok(it)
            }
            ?: Err(ProductoError.ProductoNoEliminado("Producto no eliminado con id: $id"))
    }

    override fun import(csvFile: File): Result<Unit, ProductoError> {
        return productosStorage.load(csvFile).andThen { productos ->
            productos.forEach { p ->
                productosRepository.save(p)
            }
            Ok(Unit)
        }
    }

}