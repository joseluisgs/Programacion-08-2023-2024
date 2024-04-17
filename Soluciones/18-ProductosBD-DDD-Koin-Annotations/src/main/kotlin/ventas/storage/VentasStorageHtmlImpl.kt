package dev.joseluisgs.ventas.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.locale.toDefaultDateTimeString
import dev.joseluisgs.locale.toDefaultMoneyString
import dev.joseluisgs.ventas.errors.VentaError
import dev.joseluisgs.ventas.models.Venta
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.io.File

private val logger = logging()

@Singleton
@Named("VentasStorageHtml")
class VentasStorageHtmlImpl : VentasStorage {
    override fun export(venta: Venta, file: File): Result<Unit, VentaError> {
        // to HTML
        return try {
            val html = """
            <html>
                <head>
                    <title>Venta</title>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
                </head>
                <body>
                    <div class="container">
                        <h1>Venta</h1>
                        <p>Fecha: ${venta.createdAt.toDefaultDateTimeString()}</p>
                        <p>Cliente: ${venta.cliente.nombre}</p>
                        <p>Productos:</p>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Cantidad</th>
                                    <th>Precio Unitario</th>
                                    <th>Precio Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${
                venta.lineas.joinToString("") {
                    "<tr><td>${it.producto.nombre}</td><td>${it.cantidad}</td><td>${it.producto.precio.toDefaultMoneyString()}</td><td>${(it.cantidad * it.producto.precio).toDefaultMoneyString()}</td></tr>"
                }
            }
                            </tbody>
                        </table>
                        <p class="text-right lead">Total: <span style="font-weight: bold;">${venta.total.toDefaultMoneyString()}</span></p>
                    </div>
                </body>
            </html>
        """.trimIndent()
            Ok(file.writeText(html, Charsets.UTF_8))
        } catch (e: Exception) {
            logger.error { "Error al salvar ventas a fichero: ${file.absolutePath}. ${e.message}" }
            Err(VentaError.VentaStorageError("Error al salvar ventas a fichero: ${file.absolutePath}. ${e.message}"))
        }
    }

}