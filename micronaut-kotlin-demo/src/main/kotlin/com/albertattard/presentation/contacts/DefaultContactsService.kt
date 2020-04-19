package com.albertattard.presentation.contacts

import java.util.UUID
import javax.inject.Singleton

@Singleton
class DefaultContactsService : ContactsService {

    override fun create( contact: CreateContact) : CreatedContact {
        TODO("Remember to write a test first!!")
    }

    override fun findById(id: UUID): Contact? {
        TODO("Remember to write a test first!!")
    }

    override fun list(): List<Contact> {
        TODO("Remember to write a test first!!")
    }
}
