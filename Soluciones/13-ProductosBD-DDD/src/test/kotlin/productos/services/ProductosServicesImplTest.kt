package productos.services

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dev.joseluisgs.cache.errors.CacheError
import dev.joseluisgs.productos.cache.ProductosCache
import dev.joseluisgs.productos.errors.ProductoError
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.reposiitories.ProductosRepository
import dev.joseluisgs.productos.services.ProductosServicesImpl
import dev.joseluisgs.productos.storage.ProductosStorage
import dev.joseluisgs.productos.validators.ProductoValidator
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

/*
Faltan muchos test, deberías probar todos los métodos de la clase, y todos los casos posibles
como que esté en cache, que no esté, que se actualice, que no se actualice, que se elimine, que no se elimine, etc.
que pase el validador, que no pase, etc.
y los errores que se puedan dar.
Ademas del Storage, que no se ha probado
 */

@ExtendWith(MockKExtension::class)
class ProductosServicesImplTest {

    @MockK
    private lateinit var mockRepository: ProductosRepository

    @MockK
    private lateinit var mockStorage: ProductosStorage

    @MockK
    private lateinit var mockCache: ProductosCache

    @MockK
    private lateinit var mockValidator: ProductoValidator

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

        every { mockCache.get(id) } returns Err(CacheError("Item no encontrado en cache"))
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
        verify(exactly = 1) { mockCache.get(id) }
    }

    @Test
    fun `getById throws ProductoNoEncontrado exception when producto not found`() {
        val id = 1L

        every { mockCache.get(id) } returns Err(CacheError("Item no encontrado en cache"))
        every { mockRepository.findById(id) } returns null

        val error = service.getById(id).error

        assertAll(
            { assertTrue(error is ProductoError.ProductoNoEncontrado) },
            { assertEquals("Producto no encontrado con id: $id", error.message) }
        )

        verify(exactly = 1) { mockRepository.findById(id) }
        verify(exactly = 1) { mockCache.get(id) }
    }

    @Test
    fun save() {
        val newProducto = Producto(
            nombre = "Nuevo Producto",
            precio = 20.0,
            stock = 10,
            categoria = Categoria.ELECTRONICA
        )

        every { mockValidator.validate(newProducto) } returns Ok(newProducto)
        every { mockRepository.save(newProducto) } returns newProducto.copy(id = 1)
        every { mockCache.put(1, newProducto.copy(id = 1)) } returns Ok(newProducto.copy(id = 1))

        val producto = service.create(newProducto).value

        assertAll(
            { assertEquals(1, producto.id) },
            { assertEquals("Nuevo Producto", producto.nombre) },
            { assertEquals(20.0, producto.precio) },
            { assertEquals(10, producto.stock) },
            { assertEquals(Categoria.ELECTRONICA, producto.categoria) }
        )

        verify(exactly = 1) { mockRepository.save(newProducto) }
        verify(exactly = 1) { mockValidator.validate(newProducto) }
        verify(exactly = 1) { mockCache.put(1, newProducto.copy(id = 1)) }
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
        every { mockValidator.validate(updatedProducto) } returns Ok(updatedProducto)
        every { mockCache.put(id, updatedProducto.copy(id = id)) } returns Ok(updatedProducto.copy(id = id))

        val producto = service.update(id, updatedProducto).value

        assertAll(
            { assertEquals(1, producto.id) },
            { assertEquals("Producto Actualizado", producto.nombre) },
            { assertEquals(25.0, producto.precio) },
            { assertEquals(8, producto.stock) },
            { assertEquals(Categoria.MODA, producto.categoria) }
        )

        verify(exactly = 1) { mockRepository.update(id, updatedProducto) }
        verify(exactly = 1) { mockValidator.validate(updatedProducto) }
        verify(exactly = 1) { mockCache.put(id, updatedProducto.copy(id = id)) }
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
        every { mockValidator.validate(updatedProducto) } returns Ok(updatedProducto)

        val error = service.update(id, updatedProducto).error

        assertAll(
            { assertTrue(error is ProductoError.ProductoNoActualizado) },
            { assertEquals("Producto no actualizado con id: $id", error.message) }
        )

        verify(exactly = 1) { mockRepository.update(id, updatedProducto) }
        verify(exactly = 1) { mockValidator.validate(updatedProducto) }
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
        every { mockCache.remove(id) } returns Ok(deletedProducto.copy(id = id))

        val producto = service.delete(id).value

        assertAll(
            { assertEquals(1, producto.id) },
            { assertEquals("Producto Eliminado", producto.nombre) },
            { assertEquals(30.0, producto.precio) },
            { assertEquals(12, producto.stock) },
            { assertEquals(Categoria.DEPORTE, producto.categoria) }
        )

        verify(exactly = 1) { mockRepository.delete(id) }
        verify(exactly = 1) { mockCache.remove(id) }
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
