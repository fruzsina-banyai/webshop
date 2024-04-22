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

    fun findByParentId(parentId: UUID): List<Category> {
        if (!existsById(parentId)){
            throw EntityNotFoundException(parentId.toString(), Category::class.java)
        }
        return categoryRepository.findByParentId(parentId)
    }

    fun existsById(categoryId: UUID): Boolean {
        return categoryRepository.existsById(categoryId)
    }

    fun createCategory(category: Category): Category {
        if (category.parentId != null && !existsById(category.parentId)){
            throw EntityNotFoundException(category.parentId.toString(), Category::class.java)
        }
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
        if (!categoryRepository.existsById(parentId)) {
            throw EntityNotFoundException(parentId.toString(), Category::class.java)
        }
        return categoryRepository.save(category.copy(parentId = parentId))
    }

    fun removeParentFromCategory(categoryId: UUID): Category {
        val category = findCategoryById(categoryId)
        return categoryRepository.save(category.copy(parentId = null))
    }

    fun getProducts(categoryId: UUID): List<Product> {
        if (!existsById(categoryId)) {
            throw EntityNotFoundException(categoryId.toString(), Category::class.java)
        }
        return productService.findByCategoryId(categoryId) + getProductsOfChildCategory(categoryId)
    }

    fun getProductsOfChildCategory(categoryId: UUID): List<Product> {
        return findByParentId(categoryId).flatMap { productService.findByCategoryId(it.id!!) }
    }


    fun removeProducts(categoryId: UUID) {
        getProducts(categoryId).map { productService.uncategorizeProduct(it) }
    }

    fun findAllCategories(): List<Category> {
        return categoryRepository.findAll()
    }
}