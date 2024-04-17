package dev.joseluisgs.database

import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto

fun initDemoProductos() = listOf(
    Producto(nombre = "Laptop", precio = 1000.0, stock = 100, categoria = Categoria.ELECTRONICA),
    Producto(nombre = "Smartphone", precio = 500.0, stock = 200, categoria = Categoria.ELECTRONICA),
    Producto(nombre = "Tablet", precio = 300.0, stock = 300, categoria = Categoria.ELECTRONICA),
    Producto(nombre = "Zapatillas", precio = 100.0, stock = 400, categoria = Categoria.DEPORTE),
    Producto(nombre = "Camiseta", precio = 50.0, stock = 500, categoria = Categoria.DEPORTE),
    Producto(nombre = "Pantalón", precio = 70.0, stock = 600, categoria = Categoria.DEPORTE),
    Producto(nombre = "Reloj", precio = 200.0, stock = 700, categoria = Categoria.MODA),
    Producto(nombre = "Gafas", precio = 150.0, stock = 800, categoria = Categoria.MODA),
    Producto(nombre = "Bolso", precio = 80.0, stock = 900, categoria = Categoria.MODA),
    Producto(nombre = "Libro", precio = 20.0, stock = 1000, categoria = Categoria.OTROS),
    Producto(nombre = "Mochila", precio = 40.0, stock = 1100, categoria = Categoria.OTROS),
    Producto(nombre = "Botella", precio = 10.0, stock = 1200, categoria = Categoria.OTROS)
)