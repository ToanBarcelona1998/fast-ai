package com.example.application.di

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import com.example.application.config.ApplicationConfig
import com.example.client.AwsS3Client
import com.example.client.FastAiClient
import com.example.repository.*
import com.example.repository.interfaces.*
import com.example.services.*
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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

    single <HttpClient> {
        HttpClient() {
            install(HttpTimeout){
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 20_000
                socketTimeoutMillis = 20_000
            }
            install(ContentNegotiation){
                json()
            }
        }
    }

    // Client
    single<S3Client> { s3Client }

    single<FastAiClient> {
        FastAiClient(
            get<HttpClient>(),
            fastAIUrl = ApplicationConfig.getFastAiUrl(),
            fastApiKey = ApplicationConfig.getFastAIApiKey()
        )
    }

    single<AwsS3Client> { AwsS3Client(get<S3Client>(), ApplicationConfig.getS3BucketName()) }

    // Repository
    single<IAccountRepository> { AccountRepository() }

    single<IUserRepository> { UserRepository() }

    single<IPackageRepository> { PackageRepository() }

    single<IUserCreditRepository> { UserCreditRepository() }

    single<IPaymentProviderRepository> { PaymentProviderRepository() }

    single<IImageGeneratorRepository> { ImageGeneratorRepository() }
    //


    // Service
    single<AccountService> {
        AccountService(get<IAccountRepository>())
    }

    single<ImageService> {
        ImageService(get<IImageGeneratorRepository>())
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

    single<FastAiService> {
        FastAiService(
            get<FastAiClient>(),
            get<UploadService>(),
            get<UserCreditService>(),
            get<ImageService>(),
            ApplicationConfig.getS3BucketName()
        )
    }

    single<UserService> {
        UserService(get<IUserRepository>(), get<FastAiService>())
    }
    //
}