package com.example.domain.models.entity

data class Account(
    val id: Int,
    val email: String,
    val password: String,
    val createAt: String,
    val updateAt: String? // This property is nullable
) {
    fun copyWith(
        id: Int = this.id,
        email: String? = this.email,
        password: String? = this.password,
        createAt: String? = this.createAt,
        updateAt: String? = this.updateAt
    ): Account {
        return Account(
            id = id,
            email = email ?: this.email,
            password = password ?: this.password,
            createAt = createAt ?: this.createAt,
            updateAt = updateAt ?: this.updateAt
        )
    }
}
