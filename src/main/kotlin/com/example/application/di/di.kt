package com.example.application.di

import com.example.repository.AccountRepository
import com.example.repository.PackageRepository
import com.example.repository.UserRepository
import com.example.repository.interfaces.IAccountRepository
import com.example.repository.interfaces.IPackageRepository
import com.example.repository.interfaces.IUserRepository
import com.example.services.AccountService
import com.example.services.AuthService
import com.example.services.PackageService
import com.example.services.UserService
import org.koin.dsl.module

val injection = module {
    single<IAccountRepository> { AccountRepository() }

    single<IUserRepository> { UserRepository() }

    single<IPackageRepository> { PackageRepository() }

    single<AccountService> {
        AccountService(get<IAccountRepository>())
    }
    single<UserService> {
        UserService(get<IUserRepository>())
    }

    single<PackageService> { PackageService(get<IPackageRepository>()) }

    single<AuthService> {
        AuthService(get<AccountService>(), get<UserService>())
    }
}