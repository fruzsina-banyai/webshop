package com.gocode.webshop.productcatalog.service

import com.gocode.webshop.productcatalog.model.Category
import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.errors.EntityNotFoundException
import org.assertj.core.api.Assertions.tuple
import org.assertj.core.api.CollectionAssert.assertThatCollection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.IllegalArgumentException
import java.util.*

private const val ANY_NAME = "name"

private const val ANY_DESCRIPTION = "description"

private const val ANY_PRICE = 1.234

private const val ANY_IN_STOCK = 1.234

private const val UPDATED_NAME = "updatedName"

private const val UPDATED_DESCRIPTION = "updatedDescription"

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryServiceIT {
    @Autowired
    lateinit var categoryService: CategoryService

    @Autowired
    lateinit var productService: ProductService

    @Test
    fun `should save new category with null id`() {
        val category = createCategory()
        val createdCategory = categoryService.createCategory(category.copy(id = null))

        val dbCategory = categoryService.findCategoryById(createdCategory.id!!)

        assertNotNull(dbCategory.id)
        assertEquals(createdCategory.id, dbCategory.id)
    }

    @Test
    fun `should save new category with any id`() {
        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        val dbCategory = categoryService.findCategoryById(createdCategory.id!!)

        assertEquals(createdCategory.id, dbCategory.id)
    }

    @Test
    fun `should throw error on non-existent parent id when creating category`() {
        val parentCategory = createCategory()
        categoryService.createCategory(parentCategory)

        val category = createCategory()

        assertThrows<EntityNotFoundException> { categoryService.createCategory(category.copy(parentId = parentCategory.id)) }
    }

    @Test
    fun `should delete category`() {
        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        categoryService.deleteCategory(createdCategory.id!!)

        val dbCategories = categoryService.findAllCategories()

        assertThrows<EntityNotFoundException> { categoryService.findCategoryById(createdCategory.id!!) }
        assertThatCollection(dbCategories)
            .extracting(Category::id)
            .doesNotContain(tuple(createdCategory.id))
    }

    @Test
    fun `should deactivate category`() {
        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        val dbCategory = categoryService.deactivateCategory(createdCategory.id!!)

        assertFalse(dbCategory.active)
    }

    @Test
    fun `should activate category`() {
        val category = createCategory()
        val createdCategory = categoryService.createCategory(category.copy(active = false))

        val dbCategory = categoryService.activateCategory(createdCategory.id!!)

        assertTrue(dbCategory.active)
    }

    @Test
    fun `should throw error on null id`() {
        val category = createCategory().copy(id = null)

        assertThrows<IllegalArgumentException> { categoryService.updateCategory(category) }
    }

    @Test
    fun `should throw error on non-existent id`() {
        val category = createCategory()
        categoryService.createCategory(category)

        assertThrows<EntityNotFoundException> { categoryService.updateCategory(category) }
    }

    @Test
    fun `should update category`() {
        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        val updatedCategory = categoryService.updateCategory(createdCategory.copy(
            id = createdCategory.id,
            name = UPDATED_NAME,
            active = false,
            description = UPDATED_DESCRIPTION,
            parentId = category.id
        ))

        val dbCategory = categoryService.findCategoryById(createdCategory.id!!)

        assertEquals(createdCategory.id, dbCategory.id)
        assertEquals(updatedCategory.name, dbCategory.name)
        assertEquals(createdCategory.active, dbCategory.active)
        assertEquals(updatedCategory.description, dbCategory.description)
        assertEquals(createdCategory.parentId, dbCategory.parentId)
    }

    @Test
    fun `should throw error on non-existent parent id`() {
        val category = createCategory()
        val parentCategory = createCategory()

        val createdCategory = categoryService.createCategory(category)
        categoryService.createCategory(parentCategory)

        assertThrows<EntityNotFoundException> { categoryService.assignParentToCategory(createdCategory.id!!, parentCategory.id!!) }
    }

    @Test
    fun `should assign parent to category`() {
        val category = createCategory()
        val parentCategory = createCategory()

        val createdCategory = categoryService.createCategory(category)
        val createdParentCategory = categoryService.createCategory(parentCategory)

        categoryService.assignParentToCategory(createdCategory.id!!, createdParentCategory.id!!)

        val dbCategory = categoryService.findCategoryById(createdCategory.id!!)

        assertEquals(createdParentCategory.id, dbCategory.parentId)
    }

    @Test
    fun `should remove parent from category`() {
        val category = createCategory()
        val parentCategory = createCategory()

        val createdCategory = categoryService.createCategory(category)
        val createdParentCategory = categoryService.createCategory(parentCategory)

        categoryService.assignParentToCategory(createdCategory.id!!, createdParentCategory.id!!)

        val dbCategory = categoryService.removeParentFromCategory(createdCategory.id!!)

        assertNull(dbCategory.parentId)
    }

    @Test
    fun `should throw error on non existent id`() {
        val category = createCategory()
        categoryService.createCategory(category)

        assertThrows<EntityNotFoundException> { categoryService.getProducts(category.id!!) }
    }

    @Test
    fun `should return empty list on empty category`() {
        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        val result = categoryService.getProducts(createdCategory.id!!)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return product list`() {
        val category = createCategory()
        val product1 = createProduct()
        val product2 = createProduct()

        val createdCategory = categoryService.createCategory(category)
        val createdProduct1 = productService.createProduct(product1)
        val createdProduct2 = productService.createProduct(product2)

        productService.categorizeProduct(createdProduct1.id!!, createdCategory.id!!)
        productService.categorizeProduct(createdProduct2.id!!, createdCategory.id!!)

        val result = categoryService.getProducts(createdCategory.id!!)

        assertTrue(result.isNotEmpty())
        assertThatCollection(result)
            .extracting("id")
            .contains(createdProduct1.id, createdProduct2.id)
    }

    @Test
    fun `should remove products from category`() {
        val category = createCategory()
        val product1 = createProduct()
        val product2 = createProduct()

        val createdCategory = categoryService.createCategory(category)
        val createdProduct1 = productService.createProduct(product1)
        val createdProduct2 = productService.createProduct(product2)

        productService.categorizeProduct(createdProduct1.id!!, createdCategory.id!!)
        productService.categorizeProduct(createdProduct2.id!!, createdCategory.id!!)

        categoryService.removeProducts(createdCategory.id!!)

        val result = categoryService.getProducts(createdCategory.id!!)

        assertTrue(result.isEmpty())
    }

    companion object {
        fun createProduct(): Product {
            return Product(
                id = UUID.randomUUID(),
                categoryId = null,
                name = ANY_NAME,
                description = ANY_DESCRIPTION,
                price = ANY_PRICE,
                inStock = ANY_IN_STOCK,
            )
        }

        fun createCategory(): Category {
            return Category(
                id = UUID.randomUUID(),
                name = ANY_NAME,
                description = ANY_DESCRIPTION,
                parentId = null,
            )
        }
    }
}