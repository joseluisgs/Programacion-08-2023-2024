package dev.joseluisgs.ventas.services

import com.github.michaelbull.result.Result
import dev.joseluisgs.clientes.models.Cliente
import dev.joseluisgs.ventas.errors.VentaError
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
import java.io.File
import java.util.*

interface VentasService {
    fun getById(id: UUID): Result<Venta, VentaError>
    fun create(venta: Venta): Result<Venta, VentaError>
    fun create(cliente: Cliente, lineas: List<LineaVenta>): Result<Venta, VentaError>
    fun exportToJson(venta: Venta, jsonFile: File): Result<Unit, VentaError>
    fun exportToHtml(venta: Venta, htmlFile: File): Result<Unit, VentaError>
}