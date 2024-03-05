package com.gocode.webshop.productcatalog.dto

import java.util.*

data class CategoryDto(
    val id: UUID?,
    val name: String,
    val active: Boolean,
    val description: String,
    val parentId: UUID?
)