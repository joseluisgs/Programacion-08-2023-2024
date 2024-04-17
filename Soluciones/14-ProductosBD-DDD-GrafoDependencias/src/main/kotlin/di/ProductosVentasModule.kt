package dev.joseluisgs.di

import dev.joseluisgs.clientes.repositories.ClientesRepositoryImpl
import dev.joseluisgs.config.Config
import dev.joseluisgs.database.service.SqlDeLightManager
import dev.joseluisgs.productos.cache.ProductosCache
import dev.joseluisgs.productos.reposiitories.ClientesRepository
import dev.joseluisgs.productos.reposiitories.ProductosRepository
import dev.joseluisgs.productos.repositories.ProductosRepositoryImpl
import dev.joseluisgs.productos.services.ProductosService
import dev.joseluisgs.productos.services.ProductosServicesImpl
import dev.joseluisgs.productos.storage.ProductosStorage
import dev.joseluisgs.productos.storage.ProductosStorageImpl
import dev.joseluisgs.productos.validators.ProductoValidator
import dev.joseluisgs.ventas.repositories.VentasRepository
import dev.joseluisgs.ventas.repositories.VentasRepositoryImpl
import dev.joseluisgs.ventas.services.VentasService
import dev.joseluisgs.ventas.services.VentasServiceImpl
import dev.joseluisgs.ventas.storage.VentasStorage
import dev.joseluisgs.ventas.storage.VentasStorageHtmlImpl
import dev.joseluisgs.ventas.storage.VentasStorageJsonImpl

object ProductosVentasModule {
    fun getConfig(): Config {
        return Config()
    }

    fun getSqlDeligManager(): SqlDeLightManager {
        return SqlDeLightManager(
            databaseUrl = getConfig().databaseUrl,
            databaseInMemory = getConfig().databaseInMemory,
            databaseInitData = getConfig().databaseInitData
        )
    }

    fun getClientesRepository(): ClientesRepository {
        return ClientesRepositoryImpl(getSqlDeligManager())
    }

    fun getProductosRepository(): ProductosRepository {
        return ProductosRepositoryImpl(getSqlDeligManager())
    }

    fun getProductoValidator(): ProductoValidator {
        return ProductoValidator()
    }

    fun getProductosCacheService(): ProductosCache {
        return ProductosCache(getConfig().cacheSize)
    }

    fun getProductosStorage(): ProductosStorage {
        return ProductosStorageImpl()
    }

    fun getProductosService(): ProductosService {
        return ProductosServicesImpl(
            getProductosRepository(),
            getProductoValidator(),
            getProductosCacheService(),
            getProductosStorage()
        )
    }

    fun getVentasRepository(): VentasRepository {
        return VentasRepositoryImpl(
            getSqlDeligManager(),
            getProductosRepository(),
            getClientesRepository()
        )
    }

    fun getVentasStorageJson(): VentasStorage {
        return VentasStorageJsonImpl()
    }

    fun getVentasStorageHtml(): VentasStorage {
        return VentasStorageHtmlImpl()
    }

    fun getVentasService(): VentasService {
        return VentasServiceImpl(
            getVentasRepository(),
            getProductosRepository(),
            getClientesRepository(),
            getVentasStorageJson(),
            getVentasStorageHtml()
        )
    }

}