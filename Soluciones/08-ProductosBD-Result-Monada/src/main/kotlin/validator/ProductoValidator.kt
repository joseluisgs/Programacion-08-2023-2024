package dev.joseluisgs.validator

import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Producto
import dev.joseluisgs.result.Result

class ProductoValidator {
    fun validate(producto: Producto): Result<Producto, ProductoError> {
        return when {
            producto.nombre.isBlank() -> Result.failure(ProductoError.ProductoNoValido("El nombre no puede estar vacío"))
            producto.precio <= 0 -> Result.failure(ProductoError.ProductoNoValido("El precio no puede ser menor o igual a 0"))
            producto.stock < 0 -> Result.failure(ProductoError.ProductoNoValido("El stock no puede ser menor a 0"))
            else -> Result.success(producto)
        }
    }
}