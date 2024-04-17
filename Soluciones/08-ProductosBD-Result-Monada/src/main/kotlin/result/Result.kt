package dev.joseluisgs.result

data class Result<T, E>(val value: T?, val error: E?) {
    companion object {
        inline fun <reified T, reified E> success(value: T): Result<T, E> = Result(value, null)
        inline fun <reified T, reified E> failure(error: E): Result<T, E> = Result(null, error)
    }

    fun isSuccess(): Boolean = value != null
    fun isFailure(): Boolean = error != null

    fun getOrNull(): T? = value

    inline fun success(block: (T) -> Unit): Result<T, E> {
        if (isSuccess()) {
            block(value!!)
        }
        return this
    }

    inline fun failure(block: (E) -> Unit): Result<T, E> {
        if (isFailure()) {
            block(error!!)
        }
        return this
    }

    inline fun andThen(block: (T) -> Unit): Result<T, E> {
        if (isSuccess()) {
            block(value!!)
        }
        return this
    }
}