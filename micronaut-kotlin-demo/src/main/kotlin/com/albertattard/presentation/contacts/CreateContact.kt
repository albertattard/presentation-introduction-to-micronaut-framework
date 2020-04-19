package com.albertattard.presentation.contacts

import io.micronaut.core.annotation.Introspected

@Introspected
data class CreateContact(
    val name:String,
    val options: List<ContactOption>
)
