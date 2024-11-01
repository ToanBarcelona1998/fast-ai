package com.example.client

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream

class AwsS3Client(private val s3Client: S3Client, private val ownBucket : String) {

    private fun getUrl(key : String) : String{
        return "https://$ownBucket.s3.amazonaws.com/$key"
    }

    suspend fun upload(originKey : String , uContentType : String ,file: ByteArray) : String{
        val putObjectRequest = PutObjectRequest.invoke {
            bucket = ownBucket
            key = originKey
            contentType = uContentType
            body = ByteStream.fromBytes(file)
        }

        s3Client.putObject(putObjectRequest)

        return getUrl(originKey)
    }
}