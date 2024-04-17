package dev.joseluisgs.repositories.ventas

import dev.joseluisgs.models.Venta
import java.util.*

interface VentasRepository {
    fun findById(id: UUID): Venta?
    fun save(venta: Venta): Venta
    // Haz el resto tú!
}