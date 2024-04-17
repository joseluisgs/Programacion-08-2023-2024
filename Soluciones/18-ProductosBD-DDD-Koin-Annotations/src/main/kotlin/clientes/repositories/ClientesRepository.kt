package dev.joseluisgs.productos.reposiitories

import dev.joseluisgs.clientes.models.Cliente


interface ClientesRepository {
    fun findAll(): List<Cliente>
    fun findById(id: Long): Cliente?
    fun save(cliente: Cliente): Cliente
    fun update(id: Long, cliente: Cliente): Cliente?
    fun delete(id: Long): Cliente?
}