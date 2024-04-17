package dev.joseluisgs.ventas.storage

import com.github.michaelbull.result.Result
import dev.joseluisgs.ventas.errors.VentaError
import dev.joseluisgs.ventas.models.Venta
import java.io.File

interface VentasStorage {
    fun export(venta: Venta, file: File): Result<Unit, VentaError>
}