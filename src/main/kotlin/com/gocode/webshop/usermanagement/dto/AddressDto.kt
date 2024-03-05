package com.gocode.webshop.usermanagement.dto

import java.util.UUID

data class AddressDto(
    val id: UUID?,
    val userId: UUID,
    val deleted: Boolean,
    val country: String,
    val state: String,
    val zipCode: String,
    val city: String,
    val streetAddress: String
)