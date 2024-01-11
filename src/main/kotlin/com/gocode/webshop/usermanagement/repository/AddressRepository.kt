package com.gocode.webshop.usermanagement.repository

import com.gocode.webshop.usermanagement.model.Address
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AddressRepository : JpaRepository<Address, UUID> {
    fun findByUserId(userId: UUID): List<Address>
}
