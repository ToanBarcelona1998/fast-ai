package com.example.repository.interfaces

import com.example.domain.models.requests.UserAddRequest
import com.example.domain.models.requests.UserUpdateRequest
import com.example.domain.models.responds.User

interface IUserRepository : IAddRepository<User, UserAddRequest>, IUpdateRepository<User, UserUpdateRequest, Int>,
    IDeleteRepository<Int>, IGetRepository<User, Int> {

}