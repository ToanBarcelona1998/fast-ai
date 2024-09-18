package com.example.domain.models

abstract  class BaseResponse<T>(code : Int , message: String , data: T)

final class BaseResponseSuccessful<T>(message: String, data: T) : BaseResponse<T>(200,message , data)

final class BaseResponseError<T>(code : Int , message: String , data: T)