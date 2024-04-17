package dev.joseluisgs.di

import dev.joseluisgs.config.Config
import org.koin.dsl.module

val configModule = module {
    // Config, para la configuración de la aplicación, leeremos de un fichero de propiedades con Koin
    single {
        Config(
            _databaseUrl = getProperty("database.url", "jdbc:sqlite:productos.db"),
            _databaseInitTables = getProperty("database.init.tables", "false"),
            _databaseInitData = getProperty("database.init.data", "true"),
            _databaseInMemory = getProperty("database.inmemory", "true"),
            _storageData = getProperty("storage.data", "data"),
            _cacheSize = getProperty("cache.size", "5").toInt()
        )
    }
}