package dev.joseluisgs.database.service

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.DatabaseQueries
import dev.joseluisgs.database.AppDatabase
import dev.joseluisgs.database.data.initDemoClientes
import dev.joseluisgs.database.data.initDemoProductos
import org.lighthousegames.logging.logging

private val logger = logging()

class SqlDeLightManager(
    private val databaseInMemory: Boolean = true,
    private val databaseUrl: String = "jdbc:sqlite:productos.db",
    private val databaseInitData: Boolean = true
) {
    val databaseQueries: DatabaseQueries by lazy { initQueries() }

    init {
        logger.debug { "Inicializando el gestor de Bases de Datos con SQLDelight" }
        // Inicializamos datos de ejemplo, si se ha configurado
        initialize()
    }

    private fun initQueries(): DatabaseQueries {

        return if (databaseInMemory) {
            logger.debug { "SqlDeLightClient - InMemory" }
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        } else {
            logger.debug { "SqlDeLightClient - File: $databaseUrl" }
            JdbcSqliteDriver(databaseUrl)
        }.let { driver ->
            // Creamos la base de datos
            logger.debug { "Creando Tablas (si es necesario)" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.databaseQueries

    }

    fun initialize() {
        if (databaseInitData) {
            removeAllData()
            initDataExamples()
        }
    }

    private fun initDataExamples() {
        logger.debug { "Iniciando datos de ejemplo" }
        databaseQueries.transaction {
            demoProductos()
            demoClientes()
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

    private fun demoClientes() {
        logger.debug { "Datos de ejemplo de Clientes" }
        initDemoClientes().forEach {
            databaseQueries.insertCliente(
                nombre = it.nombre,
                email = it.email,
                created_at = it.createdAt.toString(),
                updated_at = it.updatedAt.toString(),
            )
        }
    }


    // limpiamos las tablas
    private fun removeAllData() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        databaseQueries.transaction {
            databaseQueries.removeAllVentas()
            databaseQueries.removeAllClientes()
            databaseQueries.removeAllProductos()
        }
    }
}