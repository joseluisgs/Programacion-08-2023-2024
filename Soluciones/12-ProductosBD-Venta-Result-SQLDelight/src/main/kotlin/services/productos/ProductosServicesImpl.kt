package dev.joseluisgs.services.productos


import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepository
import dev.joseluisgs.validators.producto.ProductoValidator
import org.lighthousegames.logging.logging

private val logger = logging()


class ProductosServicesImpl(
    private val productosRepository: ProductosRepository,
    private val productoValidator: ProductoValidator,
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
        return productosRepository.findById(id)
            ?.let { Ok(it) }
            ?: Err(ProductoError.ProductoNoEncontrado("Producto no encontrado con id: $id"))
    }

    override fun create(producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Guardando producto: $producto" }
        return productoValidator.validate(producto).andThen {
            Ok(productosRepository.save(it))
        }
    }

    override fun update(id: Long, producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Actualizando producto por id: $id" }
        return productoValidator.validate(producto).andThen { p ->
            productosRepository.update(id, p)
                ?.let { Ok(it) }
                ?: Err(ProductoError.ProductoNoActualizado("Producto no actualizado con id: $id"))
        }
    }

    override fun delete(id: Long): Result<Producto, ProductoError> {
        logger.debug { "Borrando producto por id: $id" }
        return productosRepository.delete(id)
            ?.let { Ok(it) }
            ?: Err(ProductoError.ProductoNoEliminado("Producto no eliminado con id: $id"))
    }

}