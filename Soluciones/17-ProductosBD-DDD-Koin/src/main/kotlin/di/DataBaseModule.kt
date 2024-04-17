package dev.joseluisgs.di

import dev.joseluisgs.database.service.SqlDeLightManager
import org.koin.dsl.module

val databaseModule = module {
    // SQLManager
    single {
        SqlDeLightManager(
            databaseUrl = getProperty("database.url", "jdbc:sqlite:productos.db"),
            databaseInitData = getProperty("database.init.data", "true").toBoolean(),
            databaseInMemory = getProperty("database.inmemory", "true").toBoolean()
        )
    }
}