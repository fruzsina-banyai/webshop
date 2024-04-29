package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw EntityNotFoundException(email, com.gocode.webshop.usermanagement.model.User::class.java)
        return User(
            user.email,
            user.password,
            mutableListOf(SimpleGrantedAuthority(user.role))
        )
    }
}