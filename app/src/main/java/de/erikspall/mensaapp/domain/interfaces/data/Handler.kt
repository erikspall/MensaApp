package de.erikspall.mensaapp.domain.interfaces.data

interface Handler {
    val next: Handler?

    fun handle(request: Request)

}