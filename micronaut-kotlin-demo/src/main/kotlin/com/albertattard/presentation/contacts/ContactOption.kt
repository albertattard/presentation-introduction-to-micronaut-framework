package com.albertattard.presentation.contacts

import io.micronaut.core.annotation.Introspected

@Introspected
data class ContactOption(
  val type: Type,
  val value: String
)
