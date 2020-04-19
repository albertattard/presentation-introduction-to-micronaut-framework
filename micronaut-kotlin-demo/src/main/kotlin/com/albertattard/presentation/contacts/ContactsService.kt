package com.albertattard.presentation.contacts

import java.util.UUID

interface ContactsService {

    fun create(contact: CreateContact): CreatedContact

    fun findById(id: UUID): Contact?

    fun list(): List<Contact>
}
