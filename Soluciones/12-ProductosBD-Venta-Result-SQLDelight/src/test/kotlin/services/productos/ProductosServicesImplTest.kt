package services.productos

import dev.joseluisgs.errors.productos.ProductoError
import dev.joseluisgs.models.Categoria
import dev.joseluisgs.models.Producto
import dev.joseluisgs.repositories.productos.ProductosRepository
import dev.joseluisgs.services.productos.ProductosServicesImpl
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class ProductosServicesImplTest {

    @MockK
    private lateinit var mockRepository: ProductosRepository

    @InjectMockKs
    private lateinit var service: ProductosServicesImpl

    @Test
    fun getAll() {
        val mockProductos = listOf(
            Producto(
                id = 1,
                nombre = "Producto 1",
                precio = 10.0,
                stock = 5,
                categoria = Categoria.DEPORTE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                isDeleted = false
            )
        )

        every { mockRepository.findAll() } returns mockProductos

        val productos = service.getAll().value

        assertAll(
            { assertEquals(1, productos.size) },
            { assertEquals("Producto 1", productos[0].nombre) }
        )

        verify(exactly = 1) { mockRepository.findAll() }
    }

    @Test
    fun getByCategoria() {
        val categoria = Categoria.DEPORTE.categoria
        val mockProductos = listOf(
            Producto(
                id = 1,
                nombre = "Producto 1",
                precio = 10.0,
                stock = 5,
                categoria = Categoria.DEPORTE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                isDeleted = false
            )
        )

        every { mockRepository.findByCategoria(categoria) } returns mockProductos

        val productos = service.getByCategoria(categoria).value

        assertAll(
            { assertEquals(1, productos.size) },
            { assertEquals("Producto 1", productos[0].nombre) }
        )

        verify(exactly = 1) { mockRepository.findByCategoria(categoria) }
    }

    @Test
    fun getById() {
        val id = 1L
        val mockProducto = Producto(
            id = id,
            nombre = "Producto 1",
            precio = 10.0,
            stock = 5,
            categoria = Categoria.DEPORTE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isDeleted = false
        )

        every { mockRepository.findById(id) } returns mockProducto

        val producto = service.getById(id).value

        assertAll(
            { assertEquals(1, producto.id) },
            { assertEquals("Producto 1", producto.nombre) },
            { assertEquals(10.0, producto.precio) },
            { assertEquals(5, producto.stock) },
            { assertEquals(Categoria.DEPORTE, producto.categoria) }
        )

        verify(exactly = 1) { mockRepository.findById(id) }
    }

    @Test
    fun `getById throws ProductoNoEncontrado exception when producto not found`() {
        val id = 1L

        every { mockRepository.findById(id) } returns null

        val error = service.getById(id).error

        assertAll(
            { assertTrue(error is ProductoError.ProductoNoEncontrado) },
            { assertEquals("Producto no encontrado con id: $id", error.message) }
        )

        verify(exactly = 1) { mockRepository.findById(id) }
    }

    @Test
    fun save() {
        val newProducto = Producto(
            nombre = "Nuevo Producto",
            precio = 20.0,
            stock = 10,
            categoria = Categoria.ELECTRONICA
        )

        every { mockRepository.save(newProducto) } returns newProducto.copy(id = 1)

        val producto = service.create(newProducto).value

        assertAll(
            { assertEquals(1, producto.id) },
            { assertEquals("Nuevo Producto", producto.nombre) },
            { assertEquals(20.0, producto.precio) },
            { assertEquals(10, producto.stock) },
            { assertEquals(Categoria.ELECTRONICA, producto.categoria) }
        )

        verify(exactly = 1) { mockRepository.save(newProducto) }
    }

    @Test
    fun update() {
        val id = 1L
        val updatedProducto = Producto(
            id = id,
            nombre = "Producto Actualizado",
            precio = 25.0,
            stock = 8,
            categoria = Categoria.MODA
        )

        every { mockRepository.update(id, updatedProducto) } returns updatedProducto

        val producto = service.update(id, updatedProducto).value

        assertAll(
            { assertEquals(1, producto.id) },
            { assertEquals("Producto Actualizado", producto.nombre) },
            { assertEquals(25.0, producto.precio) },
            { assertEquals(8, producto.stock) },
            { assertEquals(Categoria.MODA, producto.categoria) }
        )

        verify(exactly = 1) { mockRepository.update(id, updatedProducto) }
    }

    @Test
    fun `update throws ProductoNoActualizado exception when producto not updated`() {
        val id = 1L
        val updatedProducto = Producto(
            id = id,
            nombre = "Producto Actualizado",
            precio = 25.0,
            stock = 8,
            categoria = Categoria.MODA
        )

        every { mockRepository.update(id, updatedProducto) } returns null

        val error = service.update(id, updatedProducto).error

        assertAll(
            { assertTrue(error is ProductoError.ProductoNoActualizado) },
            { assertEquals("Producto no actualizado con id: $id", error.message) }
        )

        verify(exactly = 1) { mockRepository.update(id, updatedProducto) }
    }

    @Test
    fun delete() {
        val id = 1L
        val deletedProducto = Producto(
            id = id,
            nombre = "Producto Eliminado",
            precio = 30.0,
            stock = 12,
            categoria = Categoria.DEPORTE
        )

        every { mockRepository.delete(id) } returns deletedProducto

        val producto = service.delete(id).value

        assertAll(
            { assertEquals(1, producto.id) },
            { assertEquals("Producto Eliminado", producto.nombre) },
            { assertEquals(30.0, producto.precio) },
            { assertEquals(12, producto.stock) },
            { assertEquals(Categoria.DEPORTE, producto.categoria) }
        )
    }

    @Test
    fun `delete throws ProductoNoEliminado exception when producto not deleted`() {
        val id = 1L

        every { mockRepository.delete(id) } returns null

        val error = service.delete(id).error

        assertAll(
            { assertTrue(error is ProductoError.ProductoNoEliminado) },
            { assertEquals("Producto no eliminado con id: $id", error.message) }
        )

        verify(exactly = 1) { mockRepository.delete(id) }
    }
}
