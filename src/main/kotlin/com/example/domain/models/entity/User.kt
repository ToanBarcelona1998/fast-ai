package com.example.domain.models.entity

data class User(
    val id: Int,
    val userName: String,
    val phoneNumber : String?,
    val gender : Int,
    val address: String?,
    val birthday : String?,
    val avatar : String?,
    val accountId : Int,
    val updateAt : String?,
    val createAt : String,
    val isActive : Boolean
){
    fun copyWith(
        id: Int? = this.id,
        userName: String = this.userName,
        phoneNumber: String? = this.phoneNumber,
        gender: Int = this.gender,
        address: String? = this.address,
        birthday: String? = this.birthday,
        avatar: String? = this.avatar,
        accountId: Int = this.accountId,
        updateAt: String? = this.updateAt,
        createAt: String = this.createAt,
        isActive: Boolean = this.isActive
    ): User {
        return User(
            id = id ?: this.id,
            userName = userName,
            phoneNumber = phoneNumber,
            gender = gender,
            address = address,
            birthday = birthday,
            avatar = avatar,
            accountId = accountId,
            updateAt = updateAt,
            createAt = createAt,
            isActive = isActive
        )
    }
}
