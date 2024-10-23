package com.example.domain.models.responds

data class Account(
    val id : Int,
    val email : String,
    val password : String,
    val createAt : String,
    val updateAt: String?,
){
    fun copyWith(
        id: Int = this.id,
        email: String = this.email,
        password: String = this.password,
        createAt: String = this.createAt,
        updateAt: String? = this.updateAt
    ): Account {
        return Account(
            id = id,
            email = email,
            password = password,
            createAt = createAt,
            updateAt = updateAt
        )
    }
}
