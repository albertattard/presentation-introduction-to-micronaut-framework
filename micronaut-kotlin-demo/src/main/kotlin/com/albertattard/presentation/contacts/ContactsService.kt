package com.albertattard.presentation.contacts

interface ContactsService {

    fun create(contact: CreateContact): CreatedContact
}
