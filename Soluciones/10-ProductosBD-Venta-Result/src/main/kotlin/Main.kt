package dev.joseluisgs

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.models.*
import dev.joseluisgs.repositories.productos.ProductosRepositoryImpl
import dev.joseluisgs.repositories.ventas.VentasRepositoryImpl
import dev.joseluisgs.services.productos.ProductosServicesImpl
import dev.joseluisgs.services.ventas.VentasServiceImpl
import dev.joseluisgs.validators.producto.ProductoValidator
import java.util.*

fun main() {
    val productosService = ProductosServicesImpl(
        ProductosRepositoryImpl(),
        ProductoValidator()
    )

    println()
    println("Buscando productos...")

    productosService.getAll()
        .onSuccess { productos ->
            productos.forEach { println(it) }
        }

    println()
    println("Buscando producto por id...")
    productosService.getById(1)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }


    println("Buscando producto por id...")
    productosService.getById(99)
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
    productosService.create(producto)
        .onSuccess { println("Creado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Creando producto...")
    val producto2 = Producto(
        nombre = "Producto 1",
        precio = -10.0,
        stock = -10,
        categoria = Categoria.DEPORTE
    )
    productosService.create(producto2)
        .onSuccess { println("Creado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Actualizando productos...")
    productosService.update(1, producto.copy(nombre = "Producto 1 Modificado"))
        .onSuccess { println("Actualizado: $it") }
        .onFailure { println("ERROR: ${it.message}") }



    println()
    println("Buscando producto...")
    productosService.getById(1)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Buscando productos...")
    productosService.getAll()
        .onSuccess { productos ->
            productos.forEach { println(it) }
        }

    println()
    println("Borrando producto por id...")
    productosService.delete(1)
        .onSuccess { println("Borrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println()
    println("Borrando producto por id...")
    productosService.delete(99)
        .onSuccess { println("Borrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }


    println()
    println("Buscando producto...")
    productosService.getById(1)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Buscando productos...")
    println(productosService.getAll())

    println()
    println("Buscando productos por categoria...")
    println(productosService.getByCategoria(Categoria.DEPORTE.name))

    val ventasService = VentasServiceImpl(
        VentasRepositoryImpl(),
        ProductosRepositoryImpl()
    )

    println()
    println("Buscando Ventas...")
    ventasService.getById(UUID.fromString("ffa315af-7333-452f-9e17-72fc9188cabc"))
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }


    println()
    println("Buscando Ventas...")
    ventasService.getById(UUID.fromString("ffa315af-7333-452f-9e17-72fc9188cabd"))
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    println()
    println("Creando Venta...")
    val cliente = Cliente(
        id = 1,
        nombre = "Juan Pérez",
        email = "juan@email.com"
    )

    val producto3 = productosService.getById(1).value
    val producto4 = productosService.getById(13).value

    val venta = Venta(
        cliente = cliente,
        lineas = listOf(
            LineaVenta(
                producto = producto3,
                cantidad = 2,
                precio = producto3.precio
            ),
            LineaVenta(
                producto = producto4,
                cantidad = 1,
                precio = producto4.precio
            )
        )
    )
    println(venta.lineas)
    println(venta)

    val res = ventasService.create(venta)
    println(res)
    println(producto3.precio * 2 + producto4.precio)
    println(venta.total)

    println()
    res.onSuccess { println("Creado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

    val venta2 = Venta(
        cliente = cliente,
        lineas = listOf(
            LineaVenta(
                producto = producto3,
                cantidad = 25,
                precio = producto3.precio
            ),
            LineaVenta(
                producto = producto4,
                cantidad = 1,
                precio = producto4.precio
            )
        )
    )

    println()
    println("Creando Venta...")
    ventasService.create(venta2)
        .onSuccess { println("Creado: $it") }
        .onFailure { println("ERROR: ${it.message}") }

}