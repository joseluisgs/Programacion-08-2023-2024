package dev.joseluisgs.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Producto

class ProductValidator {
    fun validate(product: Producto): Result<Producto, ProductoError> {
        return when {
            product.nombre.isBlank() -> Err(ProductoError.ProductoNoValido("El nombre no puede estar vacío"))
            product.precio <= 0 -> Err(ProductoError.ProductoNoValido("El precio no puede ser menor o igual a 0"))
            product.stock < 0 -> Err(ProductoError.ProductoNoValido("El stock no puede ser menor a 0"))
            else -> Ok(product)
        }
    }
}