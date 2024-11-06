package com.example.application.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JWTConfig{
    private const val SECRET = "fast_ai_secret"
    private const val ISSUER = "fast_ai_issuer"
    private const val DATETIME = 36_000_00 * 24 // 1 day

    private val algorithm = Algorithm.HMAC256(SECRET)
    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(ISSUER).build()

    fun makeJWTToken(userId : Int): String{
        val now = Date()

        return JWT
            .create()
            .withClaim("id", userId)
            .withExpiresAt(Date(now.time + DATETIME))
            .withIssuer(ISSUER)
            .sign(algorithm)
    }

    fun makeOnBoardingJWTToken(userId : Int) : String{
        val now = Date()

        return JWT
            .create()
            .withClaim("id", userId)
            .withClaim("scope" , "onboarding")
            .withExpiresAt(Date(now.time + DATETIME))
            .withIssuer(ISSUER)
            .sign(algorithm)
    }
}