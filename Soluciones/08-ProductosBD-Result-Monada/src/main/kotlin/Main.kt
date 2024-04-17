package dev.joseluisgs

import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepositoryImpl
import dev.joseluisgs.services.productos.ProductosServicesImpl
import dev.joseluisgs.validator.ProductoValidator

fun main() {
    val productosServices = ProductosServicesImpl(
        ProductosRepositoryImpl(),
        ProductoValidator()
    )

    println()
    println("Buscando productos...")

    println(productosServices.getAll().value)
    productosServices.getAll().success {
        println(it)
    }.failure {
        println("Error: ${it.message}")
    }

    println()
    println("Buscando producto por id...")
    println(productosServices.getById(1).getOrNull() ?: "Producto no encontrado")
    productosServices.getById(1).success {
        println(it)
    }.failure {
        println("Error: ${it.message}")
    }

    println()
    println("Buscando producto por id...")
    println(productosServices.getById(99).getOrNull() ?: "Producto no encontrado")
    productosServices.getById(99).success {
        println(it)
    }.failure {
        println("ERROR: ${it.message}")
    }

    println()
    println("Creando producto...")
    val producto = Producto(
        nombre = "Producto 1",
        precio = 10.0,
        stock = -1,
        categoria = Categoria.DEPORTE
    )
    productosServices.create(producto).success {
        println(it)
    }.failure {
        println("ERROR: ${it.message}")
    }

    println()
    println("Buscando productos...")
    println(productosServices.update(1, producto.copy(nombre = "Producto 1 Modificado")).value)

    println()
    println("Buscando producto...")
    println(productosServices.getById(1).value)

    println()
    println("Borrando productos...")
    println(productosServices.getAll().value)

    println()
    println("Borrando producto por id...")
    productosServices.delete(1).success {
        println(it)
    }.failure {
        println("ERROR: ${it.message}")
    }

    println()
    println("Borrando producto por id...")
    productosServices.delete(99).success {
        println(it)
    }.failure {
        println("ERROR: ${it.message}")
    }

    println()
    println("Buscando producto...")
    productosServices.getById(1).success {
        println(it)
    }.failure {
        println("Error: ${it.message}")
    }

    println()
    println("Buscando productos...")
    println(productosServices.getAll().value)

    println()
    println("Buscando productos por categoria...")
    println(productosServices.getByCategoria(Categoria.DEPORTE.name).value)

}