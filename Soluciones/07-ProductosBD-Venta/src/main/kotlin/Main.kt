package dev.joseluisgs

import dev.joseluisgs.models.*
import dev.joseluisgs.repositories.productos.ProductosRepositoryImpl
import dev.joseluisgs.repositories.ventas.VentasRepositoryImpl
import dev.joseluisgs.services.productos.ProductosServiceImpl
import dev.joseluisgs.services.ventas.VentasServiceImpl
import java.util.*

fun main() {
    val productosServices = ProductosServiceImpl(
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
    //println(productosServices.delete(1)) // No se puede borrar porque hay ventas asociadas

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

    val ventasServices = VentasServiceImpl(
        VentasRepositoryImpl(),
        ProductosRepositoryImpl()
    )

    println()
    println("Buscando Ventas...")
    println(ventasServices.getById(UUID.fromString("ffa315af-7333-452f-9e17-72fc9188cabc")))

    println()
    println("Creando Venta...")
    val cliente = Cliente(
        id = 1,
        nombre = "Juan Pérez",
        email = "juan@email.com"
    )

    val producto1 = productosServices.getById(1)
    val producto2 = productosServices.getById(13)

    val venta = Venta(
        cliente = cliente,
        lineas = listOf(
            LineaVenta(
                producto = producto1,
                cantidad = 2,
                precio = producto1.precio
            ),
            LineaVenta(
                producto = producto2,
                cantidad = 1,
                precio = producto2.precio
            )
        )
    )
    println(venta.lineas)
    println(venta)

    val res = ventasServices.create(venta)
    println(res)
    println(producto1.precio * 2 + producto2.precio)
    println(venta.total)

    println(productosServices.getById(1))
    println(productosServices.getById(13))

}