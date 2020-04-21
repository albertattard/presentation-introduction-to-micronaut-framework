package com.albertattard.presentation.contacts

import com.albertattard.presentation.contacts.DatabaseHelper.createContact
import com.albertattard.presentation.contacts.DatabaseHelper.emptyDatabase
import com.albertattard.presentation.contacts.DatabaseHelper.runAndRollback
import com.albertattard.presentation.contacts.DatabaseHelper.withContact
import java.util.UUID
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ContactsDatabaseServiceTest {

    @Autowired
    lateinit var database: Database

    lateinit var service: ContactsDatabaseService

    @BeforeEach
    internal fun setUp() {
        service = ContactsDatabaseService(database)
    }

    @Test
    fun `should add the contact and return the id`() {
        runAndRollback(database) {
            emptyDatabase()

            val create = CreateContact(name = "Albert Attard", email = "albertattard@gmail.com")
            val created = service.create(create)

            withContact(created.id) {
                assertEquals(it["name"], create.name)
                assertEquals(it["email"], create.email)
            }
        }
    }

    @Test
    fun `should return null when contact is not found`() {
        runAndRollback(database) {
            emptyDatabase()

            val found = service.findById(UUID.randomUUID())
            assertNull(found)
        }
    }

    @Test
    fun `should return the contact with the given id when found`() {
        runAndRollback(database) {
            emptyDatabase()

            val contact = createContact(CreateContact(name = "Albert Attard", email = "albertattard@gmail.com"))

            val found = service.findById(contact.id)
            assertEquals(found, contact)
        }
    }

    @Test
    fun `should return the contacts`() {
        runAndRollback(database) {
            emptyDatabase()

            val contact = createContact(CreateContact(name = "Albert Attard", email = "albertattard@gmail.com"))

            val found = service.list()
            assertEquals(found, listOf(contact))
        }
    }
}
