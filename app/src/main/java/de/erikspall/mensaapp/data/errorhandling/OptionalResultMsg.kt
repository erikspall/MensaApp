package de.erikspall.mensaapp.data.errorhandling

import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import kotlin.NoSuchElementException

internal class OptionalResultMsg<T>(
    private val msg: String,
    override val isPresent: Boolean = false,
    override val isEmpty: Boolean = true
): OptionalResult<T> {

    override fun get(): T {
        throw NoSuchElementException()
    }

    override fun orElse(def: T): T {
        return def
    }

    override fun orElseGet(supplier: Supplier<out T>): T {
        return supplier.get()
    }

    override fun getMessage(): String {
        return msg
    }

    override fun <S> map(f: (T) -> S): OptionalResult<S> {
        return OptionalResult.ofMsg(msg)
    }

    override fun <S> flatMap(f: (T) -> OptionalResult<S>): OptionalResult<S> {
        return OptionalResult.ofMsg(msg)
    }

    override fun consume(c: Consumer<T>): Optional<String> {
        return Optional.of(msg)
    }

    override fun tryToConsume(c: (T) -> Optional<String>): Optional<String> {
        return Optional.of(msg)
    }

    override fun toString(): String {
        return "No value present: $msg"
    }
}