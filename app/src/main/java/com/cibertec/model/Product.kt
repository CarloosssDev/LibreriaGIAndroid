package com.cibertec.model

import androidx.room.*

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Product {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var price: Double = 0.0
    var stock: Int = 0
    @ColumnInfo(index = true)
    var categoryId: Int = 0

    @Ignore
    constructor(id: Int, name: String, description: String, price: Double, stock: Int, categoryId: Int) {
        this.id = id
        this.name = name
        this.description = description
        this.price = price
        this.stock = stock
        this.categoryId = categoryId
    }
    constructor(name: String, description: String, price: Double, stock: Int, categoryId: Int) {
        this.name = name
        this.description = description
        this.price = price
        this.stock = stock
        this.categoryId = categoryId
    }

    companion object {
        fun getProducts(): List<Product> {
            return listOf(
                Product(1, "Product 1", "Description 1", 10.0, 10, 1),
                Product(2, "Product 2", "Description 2", 20.0, 20, 1),
                Product(3, "Product 3", "Description 3", 30.0, 30, 2),
                Product(4, "Product 4", "Description 4", 40.0, 40, 2),
                Product(5, "Product 5", "Description 5", 50.0, 50, 3),
                Product(6, "Product 6", "Description 6", 60.0, 60, 3),
                Product(7, "Product 7", "Description 7", 70.0, 70, 4),
                Product(8, "Product 8", "Description 8", 80.0, 80, 4),
                Product(9, "Product 9", "Description 9", 90.0, 90, 5),
                Product(10, "Product 10", "Description 10", 100.0, 100, 5),
            )
        }
    }
}