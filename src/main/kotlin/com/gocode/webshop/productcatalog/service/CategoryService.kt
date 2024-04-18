package com.gocode.webshop.productcatalog.service

import com.gocode.webshop.productcatalog.model.Category
import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.productcatalog.repository.CategoryRepository
import com.gocode.webshop.errors.EntityNotFoundException
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.UUID

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val productService: ProductService
) {
    fun findCategoryById(categoryId: UUID): Category {
        return categoryRepository.findById(categoryId)
            .orElseThrow { EntityNotFoundException(categoryId.toString(), Category::class.java) }
    }

    fun existsById(categoryId: UUID): Boolean {
        return categoryRepository.existsById(categoryId)
    }

    fun createCategory(category: Category): Category {
        return categoryRepository.save(category.copy(id = null))
    }

    fun deactivateCategory(categoryId: UUID): Category {
        val category = findCategoryById(categoryId)
        return categoryRepository.save(category.copy(active = false))
    }

    fun activateCategory(categoryId: UUID): Category {
        val category = findCategoryById(categoryId)
        return categoryRepository.save(category.copy(active = true))
    }

    fun deleteCategory(categoryId: UUID) {
        removeProducts(categoryId)
        categoryRepository.deleteById(categoryId)
    }

    fun updateCategory(category: Category): Category {
        if (category.id == null) {
            throw IllegalArgumentException("Can't update category with null id!")
        }
        val originalCategory = findCategoryById(category.id)
        return categoryRepository.save(
            originalCategory.copy(
                name = category.name,
                description = category.description
            )
        )
    }

    fun assignParentToCategory(categoryId: UUID, parentId: UUID): Category {
        val category = findCategoryById(categoryId)
        if (categoryRepository.existsById(parentId)) {
            return categoryRepository.save(category.copy(parentId = parentId))
        } else {
            throw EntityNotFoundException(parentId.toString(), Category::class.java)
        }
    }

    fun removeParentFormCategory(categoryId: UUID): Category {
        val category = findCategoryById(categoryId)
        return categoryRepository.save(category.copy(parentId = null))
    }

    fun getProducts(categoryId: UUID): List<Product> {
        if (existsById(categoryId)) {
            return productService.findByCategoryId(categoryId)
        } else {
            throw EntityNotFoundException(categoryId.toString(), Category::class.java)
        }
    }

    fun removeProducts(categoryId: UUID) {
        getProducts(categoryId).map { productService.uncategortizeProduct(it) }
    }

    fun findAllCategories(): List<Category> {
        return categoryRepository.findAll()
    }
}