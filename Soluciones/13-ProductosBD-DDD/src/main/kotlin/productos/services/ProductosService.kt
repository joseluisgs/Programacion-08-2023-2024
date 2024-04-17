package dev.joseluisgs.productos.services

import com.github.michaelbull.result.Result
import dev.joseluisgs.productos.errors.ProductoError
import dev.joseluisgs.productos.model.Producto
import java.io.File

interface ProductosService {
    fun getAll(): Result<List<Producto>, ProductoError>
    fun getByCategoria(categoria: String): Result<List<Producto>, ProductoError>
    fun getById(id: Long): Result<Producto, ProductoError>
    fun create(producto: Producto): Result<Producto, ProductoError>
    fun update(id: Long, producto: Producto): Result<Producto, ProductoError>
    fun delete(id: Long): Result<Producto, ProductoError>
    fun import(csvFile: File): Result<Unit, ProductoError>
}