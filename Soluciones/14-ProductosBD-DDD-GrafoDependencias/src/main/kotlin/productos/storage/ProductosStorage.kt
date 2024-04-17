package dev.joseluisgs.productos.storage

import com.github.michaelbull.result.Result
import dev.joseluisgs.productos.errors.ProductoError
import dev.joseluisgs.productos.model.Producto
import java.io.File

interface ProductosStorage {
    //fun save(list: List<Producto>, file: File): Result<Boolean, ProductoError>
    fun load(file: File): Result<List<Producto>, ProductoError>
}