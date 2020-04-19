package com.albertattard.presentation.contacts

import java.util.UUID

data class Contact(
    val id: UUID,
    val name: String,
    val email: String
)
