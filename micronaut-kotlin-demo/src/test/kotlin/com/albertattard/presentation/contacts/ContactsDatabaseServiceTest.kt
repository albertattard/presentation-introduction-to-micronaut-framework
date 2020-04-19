package com.albertattard.presentation.contacts

import com.albertattard.presentation.contacts.DatabaseHelper.createContact
import com.albertattard.presentation.contacts.DatabaseHelper.emptyDatabase
import com.albertattard.presentation.contacts.DatabaseHelper.runAndRollback
import com.albertattard.presentation.contacts.DatabaseHelper.withContact
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.micronaut.test.annotation.MicronautTest
import java.util.UUID
import org.jetbrains.exposed.sql.Database

@MicronautTest
class ContactsDatabaseServiceTest(
    private val database: Database
) : StringSpec({

    val service = ContactsDatabaseService(database)

    "should add the contact and return the id" {
        runAndRollback(database) {
            emptyDatabase()

            val create = CreateContact(name = "Albert Attard", email = "albertattard@gmail.com")
            val created = service.create(create)

            withContact(created.id) {
                it["name"] shouldBe create.name
                it["email"] shouldBe create.email
            }
        }
    }

    "should return null when contact is not found" {
        runAndRollback(database) {
            emptyDatabase()

            val found = service.findById(UUID.randomUUID())
            found shouldBe null
        }
    }

    "should return the contact with the given id when found" {
        runAndRollback(database) {
            emptyDatabase()

            val contact = createContact(CreateContact(name = "Albert Attard", email = "albertattard@gmail.com"))

            val found = service.findById(contact.id)
            found shouldBe contact
        }
    }

    "should return the contacts" {
        runAndRollback(database) {
            emptyDatabase()

            val contact = createContact(CreateContact(name = "Albert Attard", email = "albertattard@gmail.com"))

            val found = service.list()
            found shouldBe listOf(contact)
        }
    }
})
