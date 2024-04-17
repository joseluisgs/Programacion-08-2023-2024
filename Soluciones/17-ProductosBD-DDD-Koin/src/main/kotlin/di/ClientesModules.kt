package dev.joseluisgs.di

import dev.joseluisgs.clientes.repositories.ClientesRepositoryImpl
import dev.joseluisgs.productos.reposiitories.ClientesRepository
import org.koin.dsl.module

val clientesModule = module {
    // Clientes
    single<ClientesRepository> { ClientesRepositoryImpl(dbManager = get()) }
}