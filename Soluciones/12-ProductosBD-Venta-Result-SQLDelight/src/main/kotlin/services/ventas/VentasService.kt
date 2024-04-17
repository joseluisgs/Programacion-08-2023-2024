package dev.joseluisgs.services.ventas

import com.github.michaelbull.result.Result
import dev.joseluisgs.errors.ventas.VentaError
import dev.joseluisgs.models.Venta
import java.util.*

interface VentasService {
    fun getById(id: UUID): Result<Venta, VentaError>
    fun create(venta: Venta): Result<Venta, VentaError>
}