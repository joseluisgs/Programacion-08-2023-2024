package dev.joseluisgs

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.clientes.models.Cliente
import dev.joseluisgs.clientes.repositories.ClientesRepositoryImpl
import dev.joseluisgs.config.Config
import dev.joseluisgs.productos.cache.ProductosCache
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.repositories.ProductosRepositoryImpl
import dev.joseluisgs.productos.services.ProductosServicesImpl
import dev.joseluisgs.productos.storage.ProductosStorageImpl
import dev.joseluisgs.productos.validators.ProductoValidator
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
import dev.joseluisgs.ventas.repositories.VentasRepositoryImpl
import dev.joseluisgs.ventas.services.VentasServiceImpl
import dev.joseluisgs.ventas.storage.VentasStorageHtmlImpl
import dev.joseluisgs.ventas.storage.VentasStorageJsonImpl
import java.io.File
import java.util.*

fun main() {
    val productosService = ProductosServicesImpl(
        ProductosRepositoryImpl(),
        ProductoValidator(),
        ProductosCache(Config.cacheSize),
        ProductosStorageImpl(),
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
        VentasRepositoryImpl(ProductosRepositoryImpl(), ClientesRepositoryImpl()),
        ProductosRepositoryImpl(),
        ClientesRepositoryImpl(),
        VentasStorageJsonImpl(),
        VentasStorageHtmlImpl(),
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

    val producto3 = productosService.getById(3).value
    val producto4 = productosService.getById(5).value

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


    println()
    println("Buscando Ventas...")
    ventasService.getById(res.value.id)
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }


    println()
    println("Buscando Ventas...")
    ventasService.getById(UUID.fromString("ffa315af-7333-452f-9e17-72fc9188cabd"))
        .onSuccess { println("Encontrado: $it") }
        .onFailure { println("ERROR: ${it.message}") }


    val venta2 = Venta(
        cliente = cliente,
        lineas = listOf(
            LineaVenta(
                producto = producto3,
                cantidad = 999999999,
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


    val csvFile = File("data", "productos.csv")
    productosService.import(csvFile)
        .onSuccess {
            println("Importado correctamente desde $csvFile")
            productosService.getAll().value.forEach { println(it) }
        }
        .onFailure { println("ERROR: ${it.message}") }

    val jsonFile = File("data", "${res.value.id}.json")
    ventasService.exportToJson(venta, jsonFile)
        .onSuccess { println("Exportado correctamente a $jsonFile") }
        .onFailure { println("ERROR: ${it.message}") }

    val htmlFile = File("data", "${res.value.id}.html")
    ventasService.exportToHtml(venta, htmlFile)
        .onSuccess { println("Exportado correctamente a $htmlFile") }
        .onFailure { println("ERROR: ${it.message}") }

}