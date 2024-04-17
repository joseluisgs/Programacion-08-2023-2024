package dev.joseluisgs.ventas.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.ventas.dto.VentaDto
import dev.joseluisgs.ventas.errors.VentaError
import dev.joseluisgs.ventas.mappers.toVentaDto
import dev.joseluisgs.ventas.models.Venta
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.logging
import java.io.File

private val logger = logging()

class VentasStorageJsonImpl : VentasStorage {
    override fun export(venta: Venta, file: File): Result<Unit, VentaError> {
        return try {
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
            Ok(file.writeText(json.encodeToString<VentaDto>(venta.toVentaDto())))
        } catch (e: Exception) {
            logger.error { "Error al salvar ventas a fichero: ${file.absolutePath}. ${e.message}" }
            Err(VentaError.VentaStorageError("Error al salvar ventas a fichero: ${file.absolutePath}. ${e.message}"))
        }
    }
}