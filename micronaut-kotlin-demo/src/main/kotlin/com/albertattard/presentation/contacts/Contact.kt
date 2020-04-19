package com.albertattard.presentation.contacts

import io.micronaut.core.annotation.Introspected
import java.util.UUID

@Introspected
data class Contact(
    val id: UUID,
    val name: String,
    val email: String
)
