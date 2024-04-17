package dev.joseluisgs.validators.producto

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Producto

class ProductoValidator {
    fun validate(producto: Producto): Result<Producto, ProductoError> {
        return when {
            producto.nombre.isBlank() -> Err(ProductoError.ProductoNoValido("Nombre no puede estar vacío"))
            producto.precio <= 0 -> Err(ProductoError.ProductoNoValido("Precio no puede ser menor o igual a 0"))
            producto.stock < 0 -> Err(ProductoError.ProductoNoValido("Stock no puede ser menor a 0"))
            else -> Ok(producto)
        }
    }
}