package dev.joseluisgs

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.clientes.models.Cliente
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.services.ProductosService
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
import dev.joseluisgs.ventas.services.VentasService
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import org.koin.test.verify.verify
import java.io.File
import java.util.*

@OptIn(KoinExperimentalAPI::class)
fun main() {
    println("Productos Ventas con Koin")

    // Inicializamos Koin
    startKoin {
        // declare used logger
        printLogger()
        // Leemos las propiedades de un fichero
        fileProperties("/config.properties") // Por defecto busca en src/main/resources/config.properties, pero puede ser otro fichero si se lo pasas como parametro
        // declara modulos de inyección de dependencias, pero lo verificamos antes de inyectarlos
        // para asegurarnos que todo está correcto y no nos de errores
        defaultModule.verify(
            extraTypes = listOf(
                Boolean::class,
                Int::class
            )
        ) // Verificamos que los módulos están bien configurados antes de inyectarlos (ver test!)
        modules(defaultModule)
    }


    // Creamos una instancia de nuestra aplicación y la ejecutamos
    val app = ProductosVentasApp()
    app.run()

}


// Creamos una clase que implementa la interfaz KoinComponent para poder inyectar dependencias
// y poder usarlas en nuestra aplicación
// La clase ProductosVentasApp es la que se encarga de ejecutar la aplicación

class ProductosVentasApp : KoinComponent {
    fun run() {
        val productosService: ProductosService by inject()
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

        val ventasService: VentasService by inject()

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

}