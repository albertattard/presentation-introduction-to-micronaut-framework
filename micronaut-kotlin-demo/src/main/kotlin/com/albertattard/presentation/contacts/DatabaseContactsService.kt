package com.albertattard.presentation.contacts

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import javax.inject.Singleton

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
                ?.let { record ->
                    Contact(
                        id = record[ContactsTable.id].value,
                        name = record[ContactsTable.name],
                        email = record[ContactsTable.email]
                    )
                }
        }

    override fun list(): List<Contact> {
        TODO("Remember to write a test first!!")
    }
}
