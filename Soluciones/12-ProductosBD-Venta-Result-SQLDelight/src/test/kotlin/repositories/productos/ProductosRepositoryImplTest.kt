package repositories.productos

import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepositoryImpl
import dev.joseluisgs.services.database.SqlDeLightClient
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductosRepositoryImplTest {
    private val productosRepository = ProductosRepositoryImpl()


    @BeforeEach
    fun setUp() {
        SqlDeLightClient.initialize()
    }


    @Test
    fun findAll() {
        val productos = productosRepository.findAll()
        assertEquals(12, productos.size)
    }

    @Test
    fun findById() {
        val producto = productosRepository.findById(1)
        assertAll(
            { assertEquals(1, producto?.id) },
            { assertEquals("Laptop", producto?.nombre) },
            { assertEquals(1000.0, producto?.precio) },
            { assertEquals(100, producto?.stock) },
            { assertEquals("ELECTRONICA", producto?.categoria?.name) }
        )
    }

    @Test
    fun findByIdNotFound() {
        val producto = productosRepository.findById(100)
        assertEquals(null, producto)
    }

    @Test
    fun findByCategoria() {
        val productos = productosRepository.findByCategoria("OTROS")
        assertEquals(3, productos.size)
    }

    @Test
    fun findByCategoriaNotFound() {
        val productos = productosRepository.findByCategoria("KAKA")
        assertEquals(0, productos.size)
    }

    @Test
    fun save() {
        val producto = productosRepository.save(
            Producto(
                nombre = "TEST2",
                precio = 200.0,
                stock = 20,
                categoria = Categoria.MODA
            )
        )
        assertAll(
            { assertEquals(13, producto.id) },
            { assertEquals("TEST2", producto.nombre) },
            { assertEquals(200.0, producto.precio) },
            { assertEquals(20, producto.stock) },
            { assertEquals("MODA", producto.categoria.name) }
        )
    }

    @Test
    fun update() {
        val producto = productosRepository.update(
            1,
            Producto(
                id = 1,
                nombre = "TEST-UPDATE",
                precio = 200.0,
                stock = 20,
                categoria = Categoria.MODA
            )
        )
        println(producto)
        assertAll(
            { assertEquals(1, producto?.id) },
            { assertEquals("TEST-UPDATE", producto?.nombre) },
            { assertEquals(200.0, producto?.precio) },
            { assertEquals(20, producto?.stock) },
            { assertEquals("MODA", producto?.categoria?.name) }
        )
    }

    @Test
    fun updateNotFound() {
        val producto = productosRepository.update(
            -1,
            Producto(
                id = -1,
                nombre = "TEST-UPDATE",
                precio = 200.0,
                stock = 20,
                categoria = Categoria.MODA
            )
        )
        assertEquals(null, producto)
    }

    @Test
    fun delete() {
        val producto = productosRepository.delete(1)
        assertAll(
            { assertEquals(1, producto?.id) },
            { assertEquals("Laptop", producto?.nombre) },
            { assertEquals(1000.0, producto?.precio) },
            { assertEquals(100, producto?.stock) },
            { assertEquals("ELECTRONICA", producto?.categoria?.name) }
        )
    }

    @Test
    fun deleteNotFound() {
        val producto = productosRepository.delete(-1)
        assertEquals(null, producto)
    }


}
