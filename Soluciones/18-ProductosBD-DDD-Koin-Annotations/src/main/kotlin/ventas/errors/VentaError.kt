package dev.joseluisgs.ventas.errors

sealed class VentaError(val message: String) {
    class VentaNoEncontrada(message: String) : VentaError(message)
    class VentaNoValida(message: String) : VentaError(message)
    class VentaNoAlmacenada(message: String) : VentaError(message)
    class VentaStorageError(message: String) : VentaError(message)
}