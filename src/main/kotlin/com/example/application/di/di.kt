package com.example.application.di

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import com.example.application.config.ApplicationConfig
import com.example.infrastructure.AwsS3Client
import com.example.infrastructure.FastAiClient
import com.example.infrastructure.Web3Client
import com.example.repository.*
import com.example.repository.interfaces.*
import com.example.services.*
import io.ethers.providers.Provider
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val httpClient = HttpClient(CIO) {
    install(HttpTimeout) {
        requestTimeoutMillis = 20_000
        connectTimeoutMillis = 20_000
        socketTimeoutMillis = 20_000
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            encodeDefaults = true
            explicitNulls = true
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        level = LogLevel.BODY

        logger = object : Logger {
            override fun log(message: String) {
                println("KTOR CLIENT LOG:\t $message")
            }
        }
    }
}

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

    val web3 = Provider.fromUrl(ApplicationConfig.getWeb3ApiUrl()).unwrap()

    single<HttpClient> { httpClient }

    single<Provider> { web3  }

    // Client
    single<S3Client> { s3Client }

    single<Web3Client> { Web3Client(get<Provider>())}

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

    single<IAITaskRepository> { AITaskRepository() }

    single<IPurchaseRepository> { PurchaseRepository() }

    single<IModelRepository> { ModelRepository() }
    //


    // Service
    single<AccountService> {
        AccountService(get<IAccountRepository>())
    }

    single<AITaskService> {
        AITaskService(get<IAITaskRepository>())
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

    single<PurchaseService> { PurchaseService(get<IPurchaseRepository>(), get<PackageService>()) }

    single<UploadService> {
        UploadService(get<AwsS3Client>())
    }

    single<FastAiService> {
        FastAiService(
            get<FastAiClient>(),
            get<UploadService>(),
            get<UserCreditService>(),
            get<AITaskService>(),
            ApplicationConfig.getS3BucketName()
        )
    }

    single<UserService> {
        UserService(get<IUserRepository>(), get<FastAiService>(), get<AITaskService>() , get<UserCreditService>())
    }

    single<ModelService> { ModelService(get<IModelRepository>()) }

    single<TransactionService> { TransactionService(get<PurchaseService>(), get<UserCreditService>() , get<Web3Client>()) }
    //
}