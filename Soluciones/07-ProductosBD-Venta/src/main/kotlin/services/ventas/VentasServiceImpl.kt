package dev.joseluisgs.services.ventas

import dev.joseluisgs.exceptions.ventas.VentasExceptions
import dev.joseluisgs.models.LineaVenta
import dev.joseluisgs.models.Venta
import dev.joseluisgs.repositories.productos.ProductosRepository
import dev.joseluisgs.repositories.ventas.VentasRepository
import org.lighthousegames.logging.logging
import java.util.*

private val logger = logging()

class VentasServiceImpl(
    private val ventasRepository: VentasRepository,
    private val productosRepository: ProductosRepository
) : VentasService {
    override fun getById(id: UUID): Venta {
        logger.debug { "Obteniendo venta por id: $id" }
        return ventasRepository.findById(id)
            ?: throw VentasExceptions.VentaNoEncontrada("Venta no encontrada con id: $id")
    }

    override fun create(venta: Venta): Venta {
        logger.debug { "Creando venta: $venta" }

        // Validamos la venta
        validateCliente(venta)
        validateLineas(venta.lineas)

        // Actualizamos el stock de los productos
        actualizarStock(venta.lineas)

        // Guardamos la venta
        return ventasRepository.save(venta)
    }

    private fun validateCliente(venta: Venta) {
        // Hay que validar que el cliente exista en el repositorio de clientes
        return
    }

    private fun validateLineas(lineas: List<LineaVenta>) {
        // Hay que validar que los productos existan en el repositorio de productos
        lineas.forEach {
            productosRepository.findById(it.producto.id)
                ?: throw VentasExceptions.VentaNoValida("Producto no encontrado con id: ${it.producto.id}")
        }
        // Debemos validar que la cantidad de productos sea mayor que 0 y menor que el stock
        lineas.forEach {
            if (it.cantidad <= 0) {
                throw VentasExceptions.VentaNoValida("La cantidad de productos debe ser mayor que 0")
            }
            productosRepository.findById(it.producto.id)?.let { producto ->
                if (it.cantidad > producto.stock) {
                    throw VentasExceptions.VentaNoValida("No hay suficiente stock para el producto: ${producto.nombre}, stock: ${producto.stock} cantidad: ${it.cantidad}")
                }
            }
        }
    }

    private fun actualizarStock(lineas: List<LineaVenta>) {
        lineas.forEach {
            productosRepository.findById(it.producto.id)?.let { producto ->
                val updateProudcto = producto.copy(stock = producto.stock - it.cantidad)
                productosRepository.update(producto.id, updateProudcto)
                    ?: throw VentasExceptions.VentaNoValida("Error al actualizar el stock del producto: ${producto.nombre}")
            }
        }
    }
}