package com.albertattard.presentation.contacts

import com.albertattard.presentation.contacts.DatabaseHelper.createContact
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ContactsControllerTest {

    @Autowired
    lateinit var client: WebTestClient

    @Autowired
    lateinit var database: Database

    @Test
    fun `should return the list of contacts`() {
        client.get()
            .uri("/contacts/")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Array<Contact>::class.java)
    }

    @Test
    fun `should return the location from where the contact can be retrieved`() {
        val contact = createContact(database, CreateContact(name = "Albert Attard", email = "albertattard@gmail.com"))

        client.get()
            .uri("/contacts/${contact.id}")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<Contact>()
            .consumeWith {
                assertEquals(contact, it.responseBody)
            }
    }
}
