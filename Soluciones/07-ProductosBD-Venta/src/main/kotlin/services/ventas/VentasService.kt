package dev.joseluisgs.services.ventas

import dev.joseluisgs.models.Venta
import java.util.*

interface VentasService {
    fun getById(id: UUID): Venta
    fun create(venta: Venta): Venta
}