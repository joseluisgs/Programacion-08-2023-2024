package dev.joseluisgs.di

import dev.joseluisgs.productos.cache.ProductosCache
import dev.joseluisgs.productos.reposiitories.ProductosRepository
import dev.joseluisgs.productos.repositories.ProductosRepositoryImpl
import dev.joseluisgs.productos.services.ProductosService
import dev.joseluisgs.productos.services.ProductosServicesImpl
import dev.joseluisgs.productos.storage.ProductosStorage
import dev.joseluisgs.productos.storage.ProductosStorageImpl
import dev.joseluisgs.productos.validators.ProductoValidator
import org.koin.dsl.module

val productosModule = module {
    // Productos
    single<ProductosRepository> { ProductosRepositoryImpl(dbManager = get()) }
    single { ProductoValidator() }
    single { ProductosCache(size = getProperty("cache.size", "5").toInt()) }
    single<ProductosStorage> { ProductosStorageImpl() }
    single<ProductosService> {
        ProductosServicesImpl(
            productosRepository = get(),
            productoValidator = get(),
            productosCache = get(),
            productosStorage = get()
        )
    }
}
