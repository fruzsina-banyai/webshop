package com.gocode.webshop.productcatalog.dto

import java.util.UUID

data class AssignParentToCategoryDto(
    val categoryId: UUID,
    val parentId: UUID
)
