package dev.joseluisgs.productos.cache

import dev.joseluisgs.cache.CacheImpl
import dev.joseluisgs.productos.model.Producto
import org.koin.core.annotation.Property
import org.koin.core.annotation.Singleton

@Singleton
class ProductosCache(
    @Property("cache.size")
    _size: String = "5"
) : CacheImpl<Long, Producto>(_size.toInt()) {
    val cacheSize: Int = _size.toInt()
}