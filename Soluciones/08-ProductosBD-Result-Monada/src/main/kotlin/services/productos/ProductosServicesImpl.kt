package dev.joseluisgs.services.productos


import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepository
import dev.joseluisgs.result.Result
import dev.joseluisgs.validator.ProductoValidator
import org.lighthousegames.logging.logging

private val logger = logging()


class ProductosServicesImpl(
    private val productosRepository: ProductosRepository,
    private val productoValidator: ProductoValidator
) : ProductosService {
    override fun getAll(): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo todos los productos" }
        return Result.success(productosRepository.findAll())
    }

    override fun getByCategoria(categoria: String): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo productos por categoria: $categoria" }
        return Result.success(productosRepository.findByCategoria(categoria))
    }

    override fun getById(id: Long): Result<Producto, ProductoError> {
        logger.debug { "Obteniendo producto por id: $id" }
        return productosRepository.findById(id)?.let {
            Result.success(it)
        } ?: Result.failure(ProductoError.ProductoNoEncontrado("Producto no encontrado con id: $id"))

    }

    override fun create(producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Guardando producto: $producto" }
        // Railway Oriented Programming
        return productoValidator.validate(producto).andThen {
            Result.success<Producto, ProductoError>(productosRepository.save(it))
        }
    }

    override fun update(id: Long, producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Actualizando producto por id: $id" }
        return productosRepository.update(id, producto)?.let {
            Result.success(it)
        } ?: Result.failure(ProductoError.ProductoNoActualizado("Producto no actualizado con id: $id"))
    }

    override fun delete(id: Long): Result<Producto, ProductoError> {
        logger.debug { "Borrando producto por id: $id" }
        return productosRepository.delete(id)?.let {
            Result.success(it)
        } ?: Result.failure(ProductoError.ProductoNoEliminado("Producto no eliminado con id: $id"))
    }

}