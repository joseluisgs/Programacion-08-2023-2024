package dev.joseluisgs.ventas.mappers

import database.LineaVentaEntity
import database.VentaEntity
import dev.joseluisgs.clientes.mappers.toClienteDto
import dev.joseluisgs.clientes.models.Cliente
import dev.joseluisgs.productos.mappers.toProductoDto
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.ventas.dto.LineaVentaDto
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
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

fun LineaVenta.toLineaVentaDto(): LineaVentaDto {
    return LineaVentaDto(
        id = this.id.toString(),
        producto = this.producto.toProductoDto(),
        cantidad = this.cantidad,
        precio = this.precio,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
    )
}

fun Venta.toVentaDto(): dev.joseluisgs.ventas.dto.VentaDto {
    return dev.joseluisgs.ventas.dto.VentaDto(
        id = this.id.toString(),
        cliente = this.cliente.toClienteDto(),
        lineas = this.lineas.map { it.toLineaVentaDto() },
        total = this.lineas.sumOf { it.precio },
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
    )
}