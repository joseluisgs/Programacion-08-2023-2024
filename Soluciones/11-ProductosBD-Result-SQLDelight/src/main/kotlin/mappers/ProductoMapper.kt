package dev.joseluisgs.mappers

import database.ProductoEntity
import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import java.time.LocalDateTime

fun ProductoEntity.toProducto(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        stock = this.stock.toInt(),
        categoria = Categoria.valueOf(this.categoria),
        createdAt = LocalDateTime.parse(this.created_at),
        updatedAt = LocalDateTime.parse(this.updated_at),
        isDeleted = this.is_deleted.toInt() == 1
    )
}