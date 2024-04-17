package dev.joseluisgs.services.productos

import com.github.michaelbull.result.Result
import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Producto

interface ProductosService {
    fun getAll(): Result<List<Producto>, ProductoError>
    fun getByCategoria(categoria: String): Result<List<Producto>, ProductoError>
    fun getById(id: Long): Result<Producto, ProductoError>
    fun create(producto: Producto): Result<Producto, ProductoError>
    fun update(id: Long, producto: Producto): Result<Producto, ProductoError>
    fun delete(id: Long): Result<Producto, ProductoError>
}