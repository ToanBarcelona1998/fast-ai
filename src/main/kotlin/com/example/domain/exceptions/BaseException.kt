package com.example.domain.exceptions

class FastAiException(val code: Int, override val message: String) : Exception(message){
    companion object{
        // - [Error constants] -//

        // Auth error code from 1000 -
        val MISSING_EMAIL_ERROR_CODE = 1000
        val MISSING_PASSWORD_ERROR_CODE = 1001
        val EMAIL_INVALID_ERROR_CODE = 1002
        val ACCOUNT_EXISTS_ERROR_CODE = 1003

        // Auth error message
        val MISSING_EMAIL_ERROR_MESSAGE = "Missing email"
        val MISSING_PASSWORD_ERROR_MESSAGE = "Missing password"
        val EMAIL_INVALID_ERROR_MESSAGE = "Email is invalid"
        val ACCOUNT_EXISTS_ERROR_MESSAGE = "Account is exists"

        // User error code from 1010 -
        val MISSING_USER_NAME_ERROR_CODE = 1010
        val USER_NOT_FOUND_ERROR_CODE = 1011

        // User error message
        val MISSING_USER_NAME_ERROR_MESSAGE = "Missing user name"
        val USER_NOT_FOUND_ERROR_MESSAGE = "User not found"


        // Common error code
        val UNKNOWN_ERROR_CODE = 9999
        val DATABASE_ERROR_CODE = 10000

        // Common error message
        val SUCCESSFUL_MESSAGE = "Successfully"
        //
    }
}