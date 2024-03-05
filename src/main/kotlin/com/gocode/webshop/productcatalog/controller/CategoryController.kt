package com.gocode.webshop.productcatalog.controller

import com.gocode.webshop.productcatalog.dto.AssignParentToCategoryDto
import com.gocode.webshop.productcatalog.dto.CategoryDto
import com.gocode.webshop.productcatalog.dto.ProductDto
import com.gocode.webshop.productcatalog.model.Category
import com.gocode.webshop.productcatalog.model.fromCategoryDto
import com.gocode.webshop.productcatalog.model.toCategoryDto
import com.gocode.webshop.productcatalog.model.toProductDto
import com.gocode.webshop.productcatalog.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping
class CategoryController(
    private val categoryService: CategoryService
) {
    fun findCategoryById(categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.findCategoryById(categoryId).toCategoryDto())
    }

    fun createCategory(categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.createCategory(Category.fromCategoryDto(categoryDto)).toCategoryDto())
    }

    fun deactivateCategory(categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.deactivateCategory(categoryId).toCategoryDto())
    }

    fun activateCategory(categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.activateCategory(categoryId).toCategoryDto())
    }

    fun deleteCategory(categoryId: UUID): ResponseEntity<Unit> {
        categoryService.deleteCategory(categoryId)
        return ResponseEntity.ok().build()
    }

    fun updateCategory(categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.updateCategory(Category.fromCategoryDto(categoryDto)).toCategoryDto())
    }

    fun assignParentToCategory(assignParentToCategoryDto: AssignParentToCategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.assignParentToCategory(assignParentToCategoryDto.categoryId, assignParentToCategoryDto.parentId).toCategoryDto())
    }

    fun removeParentFromCategory(categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.removeParentFormCategory(categoryId).toCategoryDto())
    }

    fun getProducts(categoryId: UUID): ResponseEntity<List<ProductDto>> {
        return ResponseEntity
            .ok()
            .body(categoryService.getProducts(categoryId).map { it.toProductDto() }.toList())
    }

    fun removeProducts(categoryId: UUID): ResponseEntity<Unit> {
        categoryService.removeProducts(categoryId)
        return ResponseEntity.ok().build()
    }

    fun findAllCategories(): ResponseEntity<List<CategoryDto>> {
        return ResponseEntity
            .ok()
            .body(categoryService.findAllCategories().map { it.toCategoryDto() }.toList())
    }
}