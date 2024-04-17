package dev.joseluisgs.ventas.repositories

import dev.joseluisgs.ventas.models.Venta
import java.util.*

interface VentasRepository {
    fun findById(id: UUID): Venta?
    fun save(venta: Venta): Venta
    // Haz el resto tú!
}