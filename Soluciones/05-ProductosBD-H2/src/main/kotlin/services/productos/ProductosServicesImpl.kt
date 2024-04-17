package dev.joseluisgs.services.productos


import dev.joseluisgs.exceptions.productos.ProductosExceptions
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepository
import org.lighthousegames.logging.logging

private val logger = logging()


class ProductosServicesImpl(
    private val productosRepository: ProductosRepository
) : ProductosService {
    override fun getAll(): List<Producto> {
        logger.debug { "Obteniendo todos los productos" }
        return productosRepository.findAll()
    }

    override fun getByCategoria(categoria: String): List<Producto> {
        logger.debug { "Obteniendo productos por categoria: $categoria" }
        return productosRepository.findByCategoria(categoria)
    }

    override fun getById(id: Long): Producto {
        logger.debug { "Obteniendo producto por id: $id" }
        return productosRepository.findById(id)
            ?: throw ProductosExceptions.ProductoNoEncontrado("Producto no encontrado con id: $id")
    }

    override fun create(producto: Producto): Producto {
        logger.debug { "Guardando producto: $producto" }
        return productosRepository.save(producto)
    }

    override fun update(id: Long, producto: Producto): Producto {
        logger.debug { "Actualizando producto por id: $id" }
        return productosRepository.update(id, producto)
            ?: throw ProductosExceptions.ProductoNoActualizado("Producto no actualizado con id: $id")
    }

    override fun delete(id: Long): Producto {
        logger.debug { "Borrando producto por id: $id" }
        return productosRepository.delete(id)
            ?: throw ProductosExceptions.ProductoNoEliminado("Producto no eliminado con id: $id")
    }

}