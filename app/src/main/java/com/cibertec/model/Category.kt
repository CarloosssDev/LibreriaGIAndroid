package com.cibertec.model

import androidx.room.*

@Entity(tableName = "categories")
class Category {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""
    var description: String = ""

    @Ignore
    constructor(id: Int, name: String, description: String) {
        this.id = id
        this.name = name
        this.description = description
    }
    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    companion object {
        fun getCategories() : List<Category> {
            return listOf(
                Category(1, "Category 1", "Description 1"),
                Category(2, "Category 2", "Description 2"),
                Category(3, "Category 3", "Description 3"),
                Category(4, "Category 4", "Description 4"),
                Category(5, "Category 5", "Description 5")
            )
        }
    }


}