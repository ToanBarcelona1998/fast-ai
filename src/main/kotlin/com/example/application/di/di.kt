package com.example.application.di

import com.example.repository.*
import com.example.repository.interfaces.*
import com.example.services.*
import org.koin.dsl.module

val injection = module {
    // Repository
    single<IAccountRepository> { AccountRepository() }

    single<IUserRepository> { UserRepository() }

    single<IPackageRepository> { PackageRepository() }

    single<IUserCreditRepository> { UserCreditRepository() }

    single<IPaymentProviderRepository> { PaymentProviderRepository() }
    //


    // Service
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

    single<UserCreditService> {
        UserCreditService(get<IUserCreditRepository>())
    }


    single<PaymentProviderService> {
        PaymentProviderService(get<IPaymentProviderRepository>())
    }
    //
}