package dev.joseluisgs.productos.cache

import dev.joseluisgs.cache.CacheImpl
import dev.joseluisgs.productos.model.Producto

class ProductosCache(size: Int) : CacheImpl<Long, Producto>(size)