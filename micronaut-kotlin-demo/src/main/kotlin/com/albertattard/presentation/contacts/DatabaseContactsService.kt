package com.albertattard.presentation.contacts

import java.util.UUID
import javax.inject.Singleton
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

@Singleton
class DatabaseContactsService(
    private var database: Database
) : ContactsService {

    override fun create(contact: CreateContact): CreatedContact =
        transaction(database) {
            ContactsTable.insertAndGetId {
                it[name] = contact.name
                it[email] = contact.email
            }.value
        }.let { CreatedContact(it) }

    override fun findById(id: UUID): Contact? =
        transaction(database) {
            ContactsTable.select { ContactsTable.id eq id }
                .singleOrNull()
                ?.let { toContact(it) }
        }

    override fun list(): List<Contact> =
        transaction(database) {
            ContactsTable
                .selectAll()
                .map { toContact(it) }
        }

    private fun toContact(row: ResultRow): Contact =
        Contact(
            id = row[ContactsTable.id].value,
            name = row[ContactsTable.name],
            email = row[ContactsTable.email]
        )
}
