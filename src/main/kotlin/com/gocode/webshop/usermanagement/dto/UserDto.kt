package com.gocode.webshop.usermanagement.dto

import java.util.UUID

data class UserDto(
    val id: UUID?,
    val role: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)