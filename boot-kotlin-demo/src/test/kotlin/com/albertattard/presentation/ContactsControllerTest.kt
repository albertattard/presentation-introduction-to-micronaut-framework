package com.albertattard.presentation

import com.albertattard.presentation.contacts.Contact
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ContactsControllerTest {

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun `should return the list of contacts`() {
        client.get()
            .uri("/contacts/")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Array<Contact>::class.java)
    }
}
