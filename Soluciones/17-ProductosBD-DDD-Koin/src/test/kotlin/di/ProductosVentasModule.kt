package di

import dev.joseluisgs.di.*
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.fileProperties
import org.koin.test.check.checkModules
import org.koin.test.junit5.AutoCloseKoinTest
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class ModuleVerificationTest : AutoCloseKoinTest() {

    @Test
    fun verifyModules() {
        koinApplication {
            fileProperties("/config.properties")
            configModule.verify() // Esta en el test
            databaseModule.verify(extraTypes = listOf(Boolean::class)) // Esta en el test
            clientesModule.verify() // Esta en el test
            productosModule.verify() // Esta en el test
            ventasModule.verify() // Esta en el test
        }
    }

    @Test
    fun checkModules() {
        checkModules {
            modules(configModule, databaseModule, clientesModule, productosModule, ventasModule)
        }
    }
}