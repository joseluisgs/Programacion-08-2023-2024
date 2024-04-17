package dev.joseluisgs.exceptions.productos

sealed class ProductosExceptions(message: String) : Exception(message) {
    class ProductoNoEncontrado(message: String) : ProductosExceptions(message)
    class ProductoNoValido(message: String) : ProductosExceptions(message)
    class ProductoNoActualizado(message: String) : ProductosExceptions(message)
    class ProductoNoEliminado(message: String) : ProductosExceptions(message)
}