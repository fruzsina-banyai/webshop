package com.gocode.webshop.productcatalog.repository

import com.gocode.webshop.productcatalog.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductRepository : JpaRepository<Product, UUID> {
    fun findByCategoryId(categoryId: UUID): List<Product>
}