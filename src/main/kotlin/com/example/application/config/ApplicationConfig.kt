package com.example.application.config

import io.github.cdimascio.dotenv.dotenv

object ApplicationConfig {
    private val DATABASE_URL = "DATABASE_URL"
    private val DATABASE_NAME = "DATABASE_NAME"
    private val DATABASE_USER_NAME = "DATABASE_USER_NAME"
    private val DATABASE_PASSWORD = "DATABASE_PASSWORD"
    private val FAST_AI_URL = "FAST_AI_URL"
    private val FAST_AI_API_KEY = "FAST_AI_API_KEY"
    private val GOOGLE_API_URL = "GOOGLE_API_URL"
    private val APPLE_API_URL = "APPLE_API_URL"
    private val WEB3_API_URL = "WEB3_API_URL"
    private val S3_SECRET_KEY = "S3_SECRET_KEY"
    private val S3_ACCESS_KEY = "S3_ACCESS_KEY"

    private val dotenv = dotenv()


    fun getDatabaseUrl() : String{
        return System.getenv(DATABASE_URL) ?:  dotenv[DATABASE_URL]
    }

    fun getDatabaseName() : String{
        return System.getenv(DATABASE_NAME) ?:  dotenv[DATABASE_NAME]
    }

    fun getDatabasePassword() : String{
        return System.getenv(DATABASE_PASSWORD) ?:  dotenv[DATABASE_PASSWORD]
    }

    fun getDatabaseUserName() : String{
        return System.getenv(DATABASE_USER_NAME) ?:  dotenv[DATABASE_USER_NAME]
    }

    fun getFastAiUrl() : String{
        return System.getenv(FAST_AI_URL) ?: dotenv[FAST_AI_URL]
    }

    fun getFastAIApiKey() : String{
        return System.getenv(FAST_AI_API_KEY) ?: dotenv[FAST_AI_API_KEY]
    }

    fun getGoogleInAppPurchaseApiUrl() : String{
        return System.getenv(GOOGLE_API_URL) ?: dotenv[GOOGLE_API_URL]
    }

    fun getAppleInAppPurchaseApiUrl() : String{
        return System.getenv(APPLE_API_URL) ?: dotenv[APPLE_API_URL]
    }

    fun getWeb3ApiUrl() : String{
        return System.getenv(WEB3_API_URL) ?: dotenv[WEB3_API_URL]
    }

    fun getS3SecretKey() : String {
        return System.getenv(S3_SECRET_KEY) ?: dotenv[S3_SECRET_KEY]
    }

    fun getS3AccessKey() : String {
        return System.getenv(S3_ACCESS_KEY) ?: dotenv[S3_ACCESS_KEY]
    }
}