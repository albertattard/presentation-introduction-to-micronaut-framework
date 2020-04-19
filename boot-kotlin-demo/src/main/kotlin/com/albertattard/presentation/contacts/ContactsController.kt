package com.albertattard.presentation.contacts

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/contacts")
class ContactsController internal constructor(
    private var service: ContactsService
) {

    @GetMapping("/")
    fun list(): List<Contact> =
        service.list()

    @PostMapping("/")
    fun create(create: CreateContact): ResponseEntity<Void> =
        service.create(create)
            .let { ResponseEntity.created(URI.create("/contacts/${it.id}")).build() }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<Contact> =
        service.findById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}
