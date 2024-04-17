package dev.joseluisgs.di

import dev.joseluisgs.ventas.repositories.VentasRepository
import dev.joseluisgs.ventas.repositories.VentasRepositoryImpl
import dev.joseluisgs.ventas.services.VentasService
import dev.joseluisgs.ventas.services.VentasServiceImpl
import dev.joseluisgs.ventas.storage.VentasStorage
import dev.joseluisgs.ventas.storage.VentasStorageHtmlImpl
import dev.joseluisgs.ventas.storage.VentasStorageJsonImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module


val ventasModule = module {
    // Ventas
    single<VentasRepository> {
        VentasRepositoryImpl(
            dbManager = get(),
            productosRepository = get(),
            clientesRepository = get()
        )
    }
    single<VentasStorage>(named("json")) { VentasStorageJsonImpl() }
    single<VentasStorage>(named("html")) { VentasStorageHtmlImpl() }
    single<VentasService> {
        VentasServiceImpl(
            ventasRepository = get(),
            productosRepository = get(),
            clientesRepository = get(),
            ventasStorageJson = get(named("json")),
            ventasStorageHtml = get(named("html"))
        )
    }
}