package com.albertattard.presentation.contacts

import org.jetbrains.exposed.dao.UUIDTable

object ContactsTable : UUIDTable("contacts") {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
}
