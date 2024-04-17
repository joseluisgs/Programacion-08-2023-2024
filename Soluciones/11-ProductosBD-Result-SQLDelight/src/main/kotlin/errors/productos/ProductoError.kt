package dev.joseluisgs.errors.productos

sealed class ProductoError(val message: String) {
    class ProductoNoEncontrado(message: String) : ProductoError(message)
    class ProductoNoValido(message: String) : ProductoError(message)
    class ProductoNoActualizado(message: String) : ProductoError(message)
    class ProductoNoEliminado(message: String) : ProductoError(message)
}