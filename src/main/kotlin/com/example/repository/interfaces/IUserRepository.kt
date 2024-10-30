package com.example.repository.interfaces

import com.example.domain.models.requests.UserAddRequest
import com.example.domain.models.requests.UserUpdateRequest
import com.example.domain.models.entity.User

interface IUserRepository : IAddRepository<User, UserAddRequest>, IUpdateRepository<UserUpdateRequest, Int>,
    IDeleteRepository<Int>, IGetRepository<User, Int> {
        suspend fun getUserByAccountID(id : Int) : User?
}