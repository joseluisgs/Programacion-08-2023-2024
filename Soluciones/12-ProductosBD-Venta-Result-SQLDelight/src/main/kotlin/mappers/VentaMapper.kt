package dev.joseluisgs.mappers

import database.LineaVentaEntity
import database.VentaEntity
import dev.joseluisgs.models.Cliente
import dev.joseluisgs.models.LineaVenta
import dev.joseluisgs.models.Producto
import dev.joseluisgs.models.Venta
import java.time.LocalDateTime
import java.util.*

fun LineaVentaEntity.toLineaVenta(producto: Producto): LineaVenta {
    return LineaVenta(
        id = UUID.fromString(this.id),
        producto = producto,
        cantidad = this.cantidad.toInt(),
        precio = this.precio,
        createdAt = LocalDateTime.parse(this.created_at),
        updatedAt = LocalDateTime.parse(this.updated_at),
    )
}

fun VentaEntity.toVenta(cliente: Cliente, lineas: List<LineaVenta>): Venta {
    return Venta(
        id = UUID.fromString(this.id),
        cliente = cliente,
        lineas = lineas,
        createdAt = LocalDateTime.parse(this.created_at),
        updatedAt = LocalDateTime.parse(this.updated_at),
    )
}