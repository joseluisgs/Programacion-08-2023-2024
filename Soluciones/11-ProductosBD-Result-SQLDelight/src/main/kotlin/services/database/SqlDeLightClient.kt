package dev.joseluisgs.services.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.DatabaseQueries
import dev.joseluisgs.config.Config
import dev.joseluisgs.database.AppDatabase
import dev.joseluisgs.database.initDemoProductos
import org.lighthousegames.logging.logging

private val logger = logging()

object SqlDeLightClient {
    lateinit var databaseQueries: DatabaseQueries

    init {
        logger.debug { "Inicializando el gestor de Bases de Datos con SQLDelight" }
        initConfig()
    }

    private fun initConfig() {

        databaseQueries = if (Config.databaseInMemory) {
            logger.debug { "SqlDeLightClient - InMemory" }
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        } else {
            logger.debug { "SqlDeLightClient - File: ${Config.databaseUrl}" }
            JdbcSqliteDriver(Config.databaseUrl)
        }.let { driver ->
            // Creamos la base de datos
            logger.debug { "Creando Tablas (si es necesario)" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.databaseQueries


        // Inicializamos datos de ejemplo
        initialize()

    }

    fun initialize() {
        if (Config.databaseInitData) {
            removeAllData()
            initDataExamples()
        }
    }

    private fun initDataExamples() {
        logger.debug { "Iniciando datos de ejemplo" }
        databaseQueries.transaction {
            demoProductos()
        }
    }

    private fun demoProductos() {
        logger.debug { "Datos de ejemplo de Productos" }
        initDemoProductos().forEach {
            databaseQueries.insertProducto(
                nombre = it.nombre,
                precio = it.precio,
                stock = it.stock.toLong(),
                categoria = it.categoria.toString(),
                created_at = it.createdAt.toString(),
                updated_at = it.updatedAt.toString(),
            )
        }
    }


    // limpiamos las tablas
    private fun removeAllData() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        databaseQueries.transaction {
            databaseQueries.removeAllProductos()
        }
    }
}