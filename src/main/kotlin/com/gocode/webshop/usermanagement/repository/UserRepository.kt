package com.gocode.webshop.usermanagement.repository

import com.gocode.webshop.usermanagement.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}