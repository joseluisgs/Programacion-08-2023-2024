package dev.joseluisgs.productos.errors


sealed class ProductoError(val message: String) {
    class ProductoNoEncontrado(message: String) : ProductoError(message)
    class ProductoNoValido(message: String) : ProductoError(message)
    class ProductoNoActualizado(message: String) : ProductoError(message)
    class ProductoNoEliminado(message: String) : ProductoError(message)
    class ProductoStorageError(message: String) : ProductoError(message)
}
