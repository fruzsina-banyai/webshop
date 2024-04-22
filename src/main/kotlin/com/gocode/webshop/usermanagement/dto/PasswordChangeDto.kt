package com.gocode.webshop.usermanagement.dto

import java.util.*

data class PasswordChangeDto (
    val userId: UUID,
    val password: String
)