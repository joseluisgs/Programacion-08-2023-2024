package dev.joseluisgs

import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepositoryImpl
import dev.joseluisgs.services.productos.ProductosServicesImpl

fun main() {
    val productosServices = ProductosServicesImpl(
        ProductosRepositoryImpl()
    )

    println()
    println("Buscando productos...")

    println(productosServices.getAll())

    println()
    println("Buscando producto por id...")
    println(productosServices.getById(1))

    println()
    println("Creando producto...")
    val producto = Producto(
        nombre = "Producto 1",
        precio = 10.0,
        stock = 10,
        categoria = Categoria.DEPORTE
    )
    println(productosServices.create(producto))

    println()
    println("Buscando productos...")
    println(productosServices.update(1, producto.copy(nombre = "Producto 1 Modificado")))

    println()
    println("Buscando producto...")
    println(productosServices.getById(1))

    println()
    println("Borrando productos...")
    println(productosServices.getAll())

    println()
    println("Borrando producto por id...")
    println(productosServices.delete(1))

    try {
        println()
        println("Buscando producto...")
        println(productosServices.getById(1))
    } catch (e: Exception) {
        println("Producto no encontrado")
    }

    println()
    println("Buscando productos...")
    println(productosServices.getAll())

    println()
    println("Buscando productos por categoria...")
    println(productosServices.getByCategoria(Categoria.DEPORTE.name))

}