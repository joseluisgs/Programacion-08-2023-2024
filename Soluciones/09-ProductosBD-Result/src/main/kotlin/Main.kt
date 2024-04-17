package dev.joseluisgs

import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepositoryImpl
import dev.joseluisgs.services.productos.ProductosServicesImpl
import dev.joseluisgs.validators.ProductValidator

fun main() {
    val productosServices = ProductosServicesImpl(
        ProductosRepositoryImpl(),
        ProductValidator()
    )

    println()
    println("Buscando productos...")

    productosServices.getAll()
        .onSuccess { productos ->
            productos.forEach { println(it) }
        }.onFailure { println("ERROR: ${it.message}") }

    productosServices.getAll().mapBoth(
        success = { productos ->
            productos.forEach { println(it) }
        },
        failure = { println("ERROR: ${it.message}") }
    )

    println()
    println("Buscando producto por id...")
    productosServices.getById(1)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }


    println("Buscando producto por id...")
    productosServices.getById(99)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Creando producto...")
    val producto = Producto(
        nombre = "Producto 1",
        precio = 10.0,
        stock = 10,
        categoria = Categoria.DEPORTE
    )
    productosServices.create(producto)
        .onSuccess { println("Creado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Actualizando productos...")
    productosServices.update(1, producto.copy(nombre = "Producto 1 Modificado"))
        .onSuccess { println("Actualizado: $it") }
        .onFailure { println("ERROR: ${it.message}") }



    println()
    println("Buscando producto...")
    productosServices.getById(1)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Buscando productos...")
    productosServices.getAll()
        .onSuccess { productos ->
            productos.forEach { println(it) }
        }

    println()
    println("Borrando producto por id...")
    productosServices.delete(1)
        .onSuccess { println("Borrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println()
    println("Borrando producto por id...")
    productosServices.delete(99)
        .onSuccess { println("Borrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }


    println()
    println("Buscando producto...")
    productosServices.getById(1)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Buscando productos...")
    println(productosServices.getAll())

    println()
    println("Buscando productos por categoria...")
    println(productosServices.getByCategoria(Categoria.DEPORTE.name))

}