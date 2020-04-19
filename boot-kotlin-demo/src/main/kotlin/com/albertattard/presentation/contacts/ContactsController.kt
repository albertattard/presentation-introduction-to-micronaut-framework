package com.albertattard.presentation.contacts

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contacts")
class ContactsController internal constructor(
//    private var service: ContactsService
) {

    @GetMapping("/")
    fun list(): List<Contact> {
        return emptyList()
    }

}
