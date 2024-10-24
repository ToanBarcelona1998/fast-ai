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
        id: Int?,
        userName: String?,
        phoneNumber: String?,
        gender: Int ?,
        address: String?,
        birthday: String?,
        avatar: String?,
        updateAt: String?,
        createAt: String?,
        isActive: Boolean?,
    ): User {
        return User(
            id = id ?: this.id,
            userName = userName ?: this.userName,
            phoneNumber = phoneNumber ?: this.phoneNumber,
            gender = gender ?: this.gender,
            address = address ?: this.address,
            birthday = birthday ?: this.birthday,
            avatar = avatar,
            accountId = this.accountId,
            updateAt = updateAt ?: this.updateAt,
            createAt = createAt ?: this.createAt,
            isActive = isActive ?: this.isActive,
        )
    }
}
