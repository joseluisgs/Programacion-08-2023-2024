package services.productos

import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepository
import dev.joseluisgs.services.productos.ProductosServicesImpl
import dev.joseluisgs.validator.ProductoValidator
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class ProductosServicesImplTest {

    @MockK
    private lateinit var productosRepository: ProductosRepository

    @MockK
    private lateinit var productoValidator: ProductoValidator

    @InjectMockKs
    private lateinit var productosServices: ProductosServicesImpl

    @Test
    fun getAll() {
        val result = listOf(
            Producto(1, "Producto 1", 10.0, 10, Categoria.DEPORTE),
            Producto(2, "Producto 2", 20.0, 20, Categoria.ELECTRONICA),
            Producto(3, "Producto 3", 30.0, 30, Categoria.OTROS)
        )

        every { productosRepository.findAll() } returns result

        val productos = productosServices.getAll()

        assertAll(
            { assertTrue(productos.isSuccess()) },
            { assertEquals(productos.value?.size, 3) }
        )
    }

    @Test
    fun getByCategoria() {
    }

    @Test
    fun getById() {
        val result = Producto(1, "Producto 1", 10.0, 10, Categoria.DEPORTE)

        every { productosRepository.findById(1) } returns result

        val producto = productosServices.getById(1)

        assertAll(
            { assertTrue(producto.isSuccess()) },
            { assertEquals(producto.value?.id, 1) },
            { assertEquals(producto.value?.nombre, "Producto 1") },
            { assertEquals(producto.value?.precio, 10.0) },
            { assertEquals(producto.value?.stock, 10) },
            { assertEquals(producto.value?.categoria, Categoria.DEPORTE) }
        )
    }

    @Test
    fun getByIdNotFound() {
        every { productosRepository.findById(99) } returns null

        val producto = productosServices.getById(99)

        assertAll(
            { assertTrue(producto.isFailure()) },
            { assertTrue(producto.error is ProductoError.ProductoNoEncontrado) },
            { assertEquals(producto.error?.message, "Producto no encontrado con id: 99") }
        )
    }


    @Test
    fun create() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }
}