package de.erikspall.mensaapp.data.errorhandling

import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier


interface OptionalResult<T> {
    val isPresent: Boolean
    val isEmpty: Boolean
    fun get(): T
    fun orElse(def: T): T
    fun orElseGet(supplier: Supplier<out T>): T
    fun getMessage(): String
    fun <S> map(f: (T) -> S): OptionalResult<S>
    fun <S> flatMap(f: (T) -> OptionalResult<S>): OptionalResult<S>
    fun consume(c: Consumer<T>): Optional<String>
    fun tryToConsume(c: (T) -> Optional<String>): Optional<String>
    companion object{
        fun <T> of(value: T): OptionalResult<T> {
            return OptionalResultVal(value)
        }

        fun <T> ofMsg(msg: String): OptionalResult<T> {
            return OptionalResultMsg(msg)
        }

        fun <T> ofNullable(value: T?, msg: String): OptionalResult<T> {
            return if (value == null)
                ofMsg(msg)
            else
                of(value)
        }

        fun <T> sequence(list: List<OptionalResult<T>>): OptionalResult<List<T>> {
            if (list.isEmpty())
                return of(emptyList())

            val restSequenced = sequence(list.subList(1, list.size-1))

            return if (list[0].isPresent && restSequenced.isPresent) {
                of(mutableListOf(list[0].get()).plus(restSequenced.get()))
            } else if (list[0].isPresent && !restSequenced.isPresent) {
                restSequenced
            } else if (!list[0].isPresent && restSequenced.isPresent){
                ofMsg(list[0].getMessage())
            } else {
                ofMsg(list[0].getMessage() + System.lineSeparator() + restSequenced.getMessage())
            }
        }
    }



  /*  fun <T> ofOptional(opt: Optional<T>, msg: String): OptionalResult<T> {
        return opt.map { t ->
            OptionalResult.of(t) }.orElse(OptionalResult.ofMsg(msg))
    }*/
}