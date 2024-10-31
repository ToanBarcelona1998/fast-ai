package com.example.application.di

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import com.example.application.config.ApplicationConfig
import com.example.client.AwsS3Client
import com.example.repository.*
import com.example.repository.interfaces.*
import com.example.services.*
import io.ktor.client.*
import io.ktor.client.plugins.*
import org.koin.dsl.module

val injection = module {
    val s3Client = S3Client {
        region = ApplicationConfig.getS3Region()
        credentialsProvider = StaticCredentialsProvider(
            Credentials(
                accessKeyId = ApplicationConfig.getS3AccessKey(),
                secretAccessKey = ApplicationConfig.getS3SecretKey()
            )
        )
    }

    factory<HttpClient> {
        HttpClient(){
            defaultRequest {

            }
        }
    }

    single<S3Client> { s3Client }

    // Client
    single<AwsS3Client> { AwsS3Client(get<S3Client>(), ApplicationConfig.getS3BucketName()) }

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

    single<UploadService> {
        UploadService(get<AwsS3Client>())
    }
    //
}