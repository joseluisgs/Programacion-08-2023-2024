package dev.joseluisgs.exceptions.ventas

sealed class VentasExceptions(message: String) : Exception(message) {
    class VentaNoEncontrada(message: String) : VentasExceptions(message)
    class VentaNoValida(message: String) : VentasExceptions(message)
    class VentaNoAlmacenada(message: String) : VentasExceptions(message)
}