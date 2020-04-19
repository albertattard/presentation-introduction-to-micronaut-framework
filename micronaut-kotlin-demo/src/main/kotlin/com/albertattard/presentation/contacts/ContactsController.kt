package com.albertattard.presentation.contacts

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import java.net.URI

@Controller("/contacts")
class ContactsController internal constructor(
    private var service: ContactsService
) {

    @Post("/", produces = [MediaType.APPLICATION_JSON])
    fun create(create: CreateContact): HttpResponse<Void> =
        service.create(create)
            .let { HttpResponse.created(URI.create("/contacts/${it.id}")) }
}
