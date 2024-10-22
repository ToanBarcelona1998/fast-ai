package com.example.domain.exceptions

class FastAiException(val code: Int, override val message: String) : Exception(message){
    companion object{
        /// Status code
        // Auth error code from 1000 - 1010
        val MISSING_USER_NAME_ERROR_CODE = 1000
        val MISSING_PASSWORD_ERROR_CODE = 1001

        public val UNKNOWN_ERROR_CODE = 9999
        //

        // Message
        val MISSING_USER_ERROR_MESSAGE = "Missing user name"
        val MISSING_PASSWORD_ERROR_MESSAGE = "Missing password"

        val SUCCESSFUL_MESSAGE = "Successfully"
        //
    }
}