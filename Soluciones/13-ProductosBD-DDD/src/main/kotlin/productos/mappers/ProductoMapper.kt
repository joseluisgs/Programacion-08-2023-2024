package dev.joseluisgs.productos.mappers

import database.ProductoEntity
import dev.joseluisgs.productos.dto.ProductoDto
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
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

fun ProductoDto.toProducto(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        stock = this.stock,
        categoria = Categoria.valueOf(this.categoria),
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.updatedAt),
        isDeleted = this.isDeleted
    )
}

fun Producto.toProductoDto(): ProductoDto {
    return ProductoDto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        stock = this.stock,
        categoria = this.categoria.name,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        isDeleted = this.isDeleted
    )
}