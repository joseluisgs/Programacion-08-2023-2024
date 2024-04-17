package dev.joseluisgs.errors.ventas

sealed class VentaError(val message: String) {
    class VentaNoEncontrada(message: String) : VentaError(message)
    class VentaNoValida(message: String) : VentaError(message)
    class VentaNoAlmacenada(message: String) : VentaError(message)
}