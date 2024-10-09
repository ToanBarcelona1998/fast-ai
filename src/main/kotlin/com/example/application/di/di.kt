package com.example.application.di

import com.example.repository.AuthRepository
import com.example.repository.UserRepository
import com.example.repository.interfaces.IAuthRepository
import com.example.repository.interfaces.IUserRepository
import com.example.services.AuthService
import com.example.services.UserService
import org.koin.dsl.module

val injection = module {
    single<IAuthRepository> { AuthRepository() }

    single<IUserRepository> { UserRepository()}

    single<AuthService> {
        AuthService(get<IAuthRepository>())
    }
    single<UserService> {
        UserService(get<IUserRepository>())
    }
}