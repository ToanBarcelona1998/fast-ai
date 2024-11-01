package com.example.domain.exceptions

class FastAiException(val code: Int, override val message: String) : Exception(message){
    companion object{
        // - [Error constants] -//

        // Auth error code from 1000 -
        val MISSING_EMAIL_ERROR_CODE = 1000
        val MISSING_PASSWORD_ERROR_CODE = 1001
        val EMAIL_INVALID_ERROR_CODE = 1002
        val ACCOUNT_EXISTS_ERROR_CODE = 1003
        val ACCOUNT_NOT_EXISTS_ERROR_CODE = 1004
        val INCORRECT_PASSWORD_ERROR_CODE = 1005

        // Auth error message
        val MISSING_EMAIL_ERROR_MESSAGE = "Missing email"
        val MISSING_PASSWORD_ERROR_MESSAGE = "Missing password"
        val EMAIL_INVALID_ERROR_MESSAGE = "Email is invalid"
        val ACCOUNT_EXISTS_ERROR_MESSAGE = "Account is exists"
        val ACCOUNT_NOT_EXISTS_ERROR_MESSAGE = "Account is not exists"
        val INCORRECT_PASSWORD_ERROR_MESSAGE = "Password is not correct"

        // User error code from 1010 -
        val MISSING_USER_NAME_ERROR_CODE = 1010
        val USER_NOT_FOUND_ERROR_CODE = 1011
        val USER_BY_ACCOUNT_ID_NOT_FOUND_ERROR_CODE = 1012
        val USER_MISSING_USER_ID_ERROR_CODE = 1013
        val USER_ON_BOARDING_STATUS_ERROR_CODE = 1014
        val USER_ON_BLOCKING_STATUS_ERROR_CODE = 1015
        val USER_ON_DELETING_STATUS_ERROR_CODE = 1015

        // User error message
        val MISSING_USER_NAME_ERROR_MESSAGE = "Missing user name"
        val USER_NOT_FOUND_ERROR_MESSAGE = "User not found"
        val USER_BY_ACCOUNT_ID_NOT_FOUND_ERROR_MESSAGE = "User not found"
        val USER_MISSING_USER_ID_ERROR_MESSAGE = "Missing user id"
        val USER_ON_BOARDING_STATUS_ERROR_MESSAGE = "Account doesn't complete onboarding steps"
        val USER_ON_BLOCKING_STATUS_ERROR_MESSAGE = "Account is in blocking time"
        val USER_ON_DELETING_STATUS_ERROR_MESSAGE = "Account was deleted"


        // Package error code
        val MISSING_PACKAGE_NAME_ERROR_CODE = 1030
        val MISSING_PACKAGE_BASE_PRICE_ERROR_CODE = 1031
        val MISSING_PACKAGE_ID_ERROR_CODE = 1032
        val PACKAGE_NOT_FOUND_ERROR_CODE = 1033


        // Package error message
        val MISSING_PACKAGE_NAME_ERROR_MESSAGE = "Missing package name"
        val MISSING_PACKAGE_BASE_PRICE_ERROR_MESSAGE = "Missing base price"
        val MISSING_PACKAGE_ID_ERROR_MESSAGE = "Missing package id"
        val PACKAGE_NOT_FOUND_ERROR_MESSAGE = "Package not found"


        // User credit error code
        val USER_CREDIT_MISSING_USER_ID_ERROR_CODE = 1050
        val USER_CREDIT_NOT_FOUND_ERROR_CODE = 1051
        val USER_CREDIT_MISSING_CREDIT_CHANGE_ERROR_CODE = 1052


        // User credit error message
        val USER_CREDIT_MISSING_USER_ID_ERROR_MESSAGE = "Missing user id"
        val USER_CREDIT_NOT_FOUND_ERROR_MESSAGE = "User credit not found"
        val USER_CREDIT_MISSING_CREDIT_CHANGE_ERROR_MESSAGE = "Missing credit change"

        // Payment provider error code
        val PAYMENT_PROVIDER_MISSING_NAME_ERROR_CODE = 1070
        val PAYMENT_PROVIDER_EXISTS_ERROR_CODE = 1071
        val PAYMENT_PROVIDER_MISSING_TYPE_ERROR_CODE = 1072
        val PAYMENT_PROVIDER_TYPE_NOT_SUPPORT_ERROR_CODE = 1073

        // Payment provider error message
        val PAYMENT_PROVIDER_MISSING_NAME_ERROR_MESSAGE = "Missing method name"
        val PAYMENT_PROVIDER_EXISTS_ERROR_MESSAGE = "Payment method was exists"
        val PAYMENT_PROVIDER_MISSING_TYPE_ERROR_MESSAGE = "Missing method type"
        val PAYMENT_PROVIDER_TYPE_NOT_SUPPORT_ERROR_MESSAGE = "Method type doesn't be supported"


        // Purchase error code
        val PURCHASE_MISSING_USER_ID_ERROR_CODE = 1090
        val PURCHASE_MISSING_PACKAGE_ID_ERROR_CODE = 1091
        val PURCHASE_MISSING_METHOD_ID_ERROR_CODE = 1092
        val PURCHASE_MISSING_ID_ERROR_CODE = 1093
        val PURCHASE_MISSING_STATUS_ERROR_CODE = 1094
        val PURCHASE_STATUS_NOT_BE_ACCEPTED_ERROR_CODE = 1095
        val PURCHASE_MISSING_MULTI_REQUEST_ERROR_CODE = 1096
        val PURCHASE_PURCHASE_NOT_FOUND_ERROR_CODE = 1097
        val PURCHASE_PURCHASE_ALREADY_UPDATE_ERROR_CODE = 1098

        // Purchase error message
        val PURCHASE_MISSING_USER_ID_ERROR_MESSAGE = "Missing user id"
        val PURCHASE_MISSING_PACKAGE_ID_ERROR_MESSAGE = "Missing package id"
        val PURCHASE_MISSING_METHOD_ID_ERROR_MESSAGE = "Missing method id"
        val PURCHASE_MISSING_ID_ERROR_MESSAGE = "Missing transaction id"
        val PURCHASE_MISSING_STATUS_ERROR_MESSAGE = "Missing transaction status"
        val PURCHASE_STATUS_NOT_BE_ACCEPTED_ERROR_MESSAGE = "Transaction status has to be in [pending, completed , failed]"
        val PURCHASE_MISSING_MULTI_REQUEST_ERROR_MESSAGE = "Missing requests"
        val PURCHASE_PURCHASE_NOT_FOUND_ERROR_MESSAGE = "Transaction not found"
        val PURCHASE_PURCHASE_ALREADY_UPDATE_ERROR_MESSAGE = "Transaction was already updated"


        // Upload file error code
        val UPLOAD_FILE_TOO_LARGE_ERROR_CODE = 1110

        // Upload file error message
        val UPLOAD_FILE_TOO_LARGE_ERROR_MESSAGE = "File is too large"


        // RunWare error code
        val RUNWARE_SEND_REQUEST_ERROR_CODE = 1150
        val RUNWARE_MISSING_MODEL_ERROR_CODE = 1151
        val RUNWARE_MISSING_PROMPT_ERROR_CODE = 1152
        val RUNWARE_INVALID_TASK_ERROR_CODE = 1153
        val RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_CODE = 1154
        val RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE = 1155
        val RUNWARE_DEVELOP_ERROR_CODE = 1156

        // RunWare error message
        val RUNWARE_MISSING_MODEL_ERROR_MESSAGE = "Missing model"
        val RUNWARE_MISSING_PROMPT_ERROR_MESSAGE = "Missing prompt"
        val RUNWARE_INVALID_TASK_ERROR_MESSAGE = "Invalid task"
        val RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_MESSAGE = "Insufficient balance"
        val RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE = "Missing input image"
        val RUNWARE_DEVELOP_ERROR_MESSAGE = "Develop error"

        // Image error code
        val IMAGE_MISSING_USER_ID_ERROR_CODE = 1070
        val IMAGE_MISSING_FILE_FOMAT_ERROR_CODE = 1071
        val IMAGE_MISSING_S3_PATH_ERROR_CODE = 1072

        // Image error message
        val IMAGE_MISSING_USER_ID_ERROR_MESSAGE = "Missing user id"
        val IMAGE_MISSING_FILE_FOMAT_ERROR_MESSAGE = "Missing file format"
        val IMAGE_MISSING_S3_PATH_ERROR_MESSAGE = "Missing s3 url"

        // Common error code
        val UNKNOWN_ERROR_CODE = 9999
        val DATABASE_ERROR_CODE = 10000

        // Common error message
        val SUCCESSFUL_MESSAGE = "Successfully"
        //
    }
}