package com.gocode.webshop.productcatalog.repository

import com.gocode.webshop.productcatalog.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CategoryRepository : JpaRepository<Category, UUID> {
    fun findByParentId(parentId: UUID): List<Category>
}