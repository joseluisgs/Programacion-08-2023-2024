package dev.joseluisgs

import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.personas.PersonasRepositoryImpl
import dev.joseluisgs.services.cache.personas.PersonasCache
import dev.joseluisgs.services.personas.PersonasServiceImpl
import dev.joseluisgs.services.storage.StoragePersonasCsv
import dev.joseluisgs.services.storage.StoragePersonasJson

fun main(args: Array<String>) {
    val personasService = PersonasServiceImpl(
        storageCsv = StoragePersonasCsv(),
        storageJson = StoragePersonasJson(),
        personasRepository = PersonasRepositoryImpl(),
        personasCache = PersonasCache()
    )

    val personas = personasService.loadFromCsv()
    personas.forEach {
        // insertamos en la base de datos
        personasService.save(it)
    }

    // Mostramos los datos
    personasService.findAll().forEach {
        println(it)
    }

    // Actualizamos el 13
    val persona = personasService.findById(13)
    println(persona)
    val personaUpdated = Estudiante(13, "Updated", 23, createdAt = persona.createdAt)
    println(personasService.update(13, personaUpdated))

    // borramos el 15
    println(personasService.delete(15))

    // salvamos a JSON
    personasService.storeToJson(personasService.findByTipo("Estudiante"))

}


