package com.example.repository

import com.example.client.AwsS3Client
import com.example.repository.interfaces.IS3Repository

class S3Repository(private val awsS3Client: AwsS3Client) : IS3Repository {
    override suspend fun add(request: String): String {
        return awsS3Client.upload(request)
    }

}