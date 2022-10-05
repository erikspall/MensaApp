package de.erikspall.mensaapp.data.errorhandling

import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.NoSuchElementException

internal class OptionalResultVal<T>(
    private val value: T,
    override val isPresent: Boolean = true,
    override val isEmpty: Boolean = false
): OptionalResult<T> {

    override fun get(): T {
        return value
    }

    override fun orElse(def: T): T {
        return value
    }

    override fun orElseGet(supplier: Supplier<out T>): T {
        return value
    }

    override fun getMessage(): String {
        throw NoSuchElementException()
    }


    override fun consume(c: Consumer<T>): Optional<String> {
        c.accept(value)
        return Optional.empty()
    }


    override fun <S> map(f: (T) -> S): OptionalResult<S> {
        return OptionalResult.of(f(value))
    }

    override fun <S> flatMap(f: (T) -> OptionalResult<S>): OptionalResult<S> {
        return f(value)
    }

    override fun tryToConsume(c: (T) -> Optional<String>): Optional<String> {
        return c(value)
    }

    override fun toString(): String {
        return "Value present: $value"
    }
}