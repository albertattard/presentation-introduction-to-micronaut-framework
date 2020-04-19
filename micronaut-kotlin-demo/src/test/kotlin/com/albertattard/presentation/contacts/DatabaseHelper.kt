package com.albertattard.presentation.contacts

import java.util.UUID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseHelper {

    fun <T> runAndRollback(database: Database, block: (Transaction) -> T): T =
        transaction(database) {
            try {
                block(this)
            } finally {
                rollback()
            }
        }

    fun emptyDatabase(): Int =
        ContactsTable.deleteAll()

    fun createContact(contact: CreateContact): Contact =
        ContactsTable.insertAndGetId {
            it[name] = contact.name
            it[email] = contact.email
        }.let {
            Contact(id = it.value, name = contact.name, email = contact.email)
        }

    fun withContact(id: UUID, block: (Map<String, Any>) -> Unit): UUID =
        readContact(id).let {
            block(it)
            it["id"] as UUID
        }

    private fun readContact(id: UUID): Map<String, Any> =
        ContactsTable.select { ContactsTable.id eq id }
            .singleOrNull()
            ?.let {
                mapOf(
                    "id" to it[ContactsTable.id].value,
                    "name" to it[ContactsTable.name],
                    "email" to it[ContactsTable.email]
                )
            } ?: emptyMap()
}
