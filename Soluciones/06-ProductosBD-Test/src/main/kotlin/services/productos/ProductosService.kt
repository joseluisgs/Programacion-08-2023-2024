package dev.joseluisgs.services.productos

import dev.joseluisgs.models.Producto

interface ProductosService {
    fun getAll(): List<Producto>
    fun getByCategoria(categoria: String): List<Producto>
    fun getById(id: Long): Producto
    fun create(producto: Producto): Producto
    fun update(id: Long, producto: Producto): Producto
    fun delete(id: Long): Producto
}