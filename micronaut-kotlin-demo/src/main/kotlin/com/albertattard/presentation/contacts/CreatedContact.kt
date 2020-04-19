package com.albertattard.presentation.contacts

import io.micronaut.core.annotation.Introspected
import java.util.UUID

@Introspected
data class CreatedContact(
    val id: UUID
)
