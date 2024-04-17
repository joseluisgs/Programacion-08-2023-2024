package dev.joseluisgs.exceptions.personas

sealed class PersonasException(message: String) : Exception(message) {
    class PersonaNotFoundException(message: String) : PersonasException(message)
    class PersonaNotSavedException(message: String) : PersonasException(message)
    class PersonaNotUpdatedException(message: String) : PersonasException(message)
    class PersonaNotDeletedException(message: String) : PersonasException(message)
    class PersonasNotFetchedException(message: String) : PersonasException(message)
}