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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/category")
class CategoryController(
    private val categoryService: CategoryService
) {
    @GetMapping("/{categoryId}")
    fun findCategoryById(@PathVariable categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.findCategoryById(categoryId).toCategoryDto())
    }

    @PostMapping
    fun createCategory(categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.createCategory(Category.fromCategoryDto(categoryDto)).toCategoryDto())
    }

    @PutMapping("/{categoryId}/deactivate")
    fun deactivateCategory(categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.deactivateCategory(categoryId).toCategoryDto())
    }

    @PutMapping("/{categoryId}/activate")
    fun activateCategory(categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.activateCategory(categoryId).toCategoryDto())
    }

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(categoryId: UUID): ResponseEntity<Unit> {
        categoryService.deleteCategory(categoryId)
        return ResponseEntity.ok().build()
    }

    @PutMapping
    fun updateCategory(categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.updateCategory(Category.fromCategoryDto(categoryDto)).toCategoryDto())
    }

    @PutMapping("/assign-parent")
    fun assignParentToCategory(assignParentToCategoryDto: AssignParentToCategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.assignParentToCategory(assignParentToCategoryDto.categoryId, assignParentToCategoryDto.parentId).toCategoryDto())
    }

    @PutMapping("/remove-parent")
    fun removeParentFromCategory(categoryId: UUID): ResponseEntity<CategoryDto> {
        return ResponseEntity
            .ok()
            .body(categoryService.removeParentFormCategory(categoryId).toCategoryDto())
    }

    @GetMapping("/{categoryId}/get-products")
    fun getProducts(@PathVariable categoryId: UUID): ResponseEntity<List<ProductDto>> {
        return ResponseEntity
            .ok()
            .body(categoryService.getProducts(categoryId).map { it.toProductDto() }.toList())
    }

    @DeleteMapping("/{categoryId}/remove-products")
    fun removeProducts(@PathVariable categoryId: UUID): ResponseEntity<Unit> {
        categoryService.removeProducts(categoryId)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun findAllCategories(): ResponseEntity<List<CategoryDto>> {
        return ResponseEntity
            .ok()
            .body(categoryService.findAllCategories().map { it.toCategoryDto() }.toList())
    }
}