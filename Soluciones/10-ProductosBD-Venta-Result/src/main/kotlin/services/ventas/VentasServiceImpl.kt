package dev.joseluisgs.services.ventas

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import dev.joseluisgs.errors.ventas.VentaError
import dev.joseluisgs.models.Cliente
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
    override fun getById(id: UUID): Result<Venta, VentaError> {
        logger.debug { "Obteniendo venta por id: $id" }
        return ventasRepository.findById(id)
            ?.let { Ok(it) }
            ?: Err(VentaError.VentaNoEncontrada("Venta no encontrada con id: $id"))


    }

    override fun create(venta: Venta): Result<Venta, VentaError> {
        logger.debug { "Creando venta: $venta" }

        // Validamos la vennta
        // ROP
        return validateCliente(venta.cliente)
            .andThen { validateLineas(venta.lineas) }
            .andThen { actualizarStock(venta.lineas) }
            .andThen { Ok(ventasRepository.save(venta)) }
    }

    private fun validateCliente(cliente: Cliente): Result<Cliente, VentaError> {
        // Hay que validar que el cliente exista en el repositorio de clientes
        return Ok(cliente)
    }

    private fun validateLineas(lineas: List<LineaVenta>): Result<List<LineaVenta>, VentaError> {
        // Hay que validar que los productos existan en el repositorio de productos
        lineas.forEach {
            productosRepository.findById(it.producto.id)
                ?: return Err(VentaError.VentaNoValida("Producto no encontrado con id: ${it.producto.id}"))
        }
        // Debemos validar que la cantidad de productos sea mayor que 0 y menor que el stock
        lineas.forEach {
            if (it.cantidad <= 0) {
                return Err(VentaError.VentaNoValida("La cantidad de productos debe ser mayor que 0"))
            }
            productosRepository.findById(it.producto.id)?.let { producto ->
                if (it.cantidad > producto.stock) {
                    return Err(VentaError.VentaNoValida("No hay suficiente stock para el producto: ${producto.nombre}, stock: ${producto.stock} cantidad: ${it.cantidad}"))
                }
            }
        }
        return Ok(lineas)
    }

    private fun actualizarStock(lineas: List<LineaVenta>): Result<List<LineaVenta>, VentaError> {
        lineas.forEach {
            productosRepository.findById(it.producto.id)?.let { producto ->
                val updateProducto = producto.copy(stock = producto.stock - it.cantidad)
                productosRepository.update(producto.id, updateProducto)
                    ?: return Err(VentaError.VentaNoValida("Error al actualizar el stock del producto: ${producto.nombre}"))
            }
        }
        return Ok(lineas)
    }
}