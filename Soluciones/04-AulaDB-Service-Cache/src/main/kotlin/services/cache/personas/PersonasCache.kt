package dev.joseluisgs.services.cache.personas

import dev.joseluisgs.config.Config
import dev.joseluisgs.models.Persona
import dev.joseluisgs.services.cache.base.Cache
import org.lighthousegames.logging.logging

private val logger = logging()

class PersonasCache : Cache<Persona, Long> {
    private val cache: MutableMap<Long, Persona> = mutableMapOf()
    override fun put(key: Long, value: Persona) {
        logger.debug { "Guardando persona en cache con id $key" }
        // Es de tamaño fijo, si se llena, eliminamos el primero
        if (cache.size >= Config.cacheSize && !cache.containsKey(key)) {
            val firstKey = cache.keys.first()
            logger.debug { "Eliminando persona en cache con id $firstKey porque está llena" }
            cache.remove(firstKey)
        }
        cache[key] = value
    }

    override fun get(key: Long): Persona? {
        logger.debug { "Obteniendo persona en cache con id $key" }
        return cache[key]
    }

    override fun remove(key: Long): Persona? {
        logger.debug { "Eliminando persona en cache con id $key" }
        return cache.remove(key)
    }

    override fun clear() {
        logger.debug { "Limpiando cache de Personas" }
        cache.clear()
    }
}