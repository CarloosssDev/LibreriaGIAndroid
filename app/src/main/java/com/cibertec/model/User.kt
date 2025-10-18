package com.cibertec.model

import androidx.room.*

@Entity(tableName = "users")
class User {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var name: String = ""
    var phone: String = ""
    var username: String = ""
    var password: String =""

    @Ignore
    constructor(id: Int, name: String, phone: String, username: String, password: String) {
        this.id = id
        this.name = name
        this.phone = phone
        this.username = username
        this.password = password
    }
    constructor(name: String, phone: String, username: String, password: String) {
        this.name = name
        this.phone = phone
        this.username = username
        this.password = password
    }

    companion object {
        fun getUsers() : List<User> {
            return listOf(
                User(1, "José Silva", "123456789", "jose", "1234"),
                User(2, "Ricardo Bellido", "987654321", "ricardo", "4321"),
                User(3, "Nanci Cabrera", "147258369", "nanci", "1234")
            )
        }
    }

}