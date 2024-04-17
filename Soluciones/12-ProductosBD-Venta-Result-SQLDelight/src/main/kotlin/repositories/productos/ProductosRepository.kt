package dev.joseluisgs.repositories.productos

import dev.joseluisgs.models.Producto

interface ProductosRepository {
    fun findAll(): List<Producto>
    fun findById(id: Long): Producto?
    fun findByCategoria(categoria: String): List<Producto>
    fun save(producto: Producto): Producto
    fun update(id: Long, producto: Producto): Producto?
    fun delete(id: Long): Producto?
}