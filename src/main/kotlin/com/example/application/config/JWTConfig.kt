package com.example.application.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import java.util.*

object JWTConfig {
    private const val SECRET = "fast_ai_secret"
    private const val ISSUER = "fast_ai_issuer"
    private const val DATETIME = 36_000_00L * 24 // 1 day

    private val algorithm = Algorithm.HMAC256(SECRET)
    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(ISSUER).build()

    private fun makeJWT(userId: Int, expiredDate: Long): JWTCreator.Builder {
        val now = Date()
        return JWT
            .create()
            .withClaim("id", userId)
            .withExpiresAt(Date(now.time + expiredDate))
            .withIssuer(ISSUER)
    }

    fun makeJWTToken(userId: Int): String {
        return makeJWT(userId, DATETIME * 7)
            .sign(algorithm)
    }

    fun makeJWTRefreshToken(userId: Int): String {
        return makeJWT(userId, DATETIME * 30)
            .withClaim("scope" , "refresh")
            .sign(algorithm)
    }

    fun makeOnBoardingJWTToken(userId: Int): String {
        return makeJWT(userId, DATETIME)
            .withClaim("scope", "onboarding")
            .sign(algorithm)
    }

    fun getClaims(token : String) : Map<String, Claim>?{
        try{
            val decode = verifier.verify(token)

            return decode.claims
        }catch (e: Exception){
            return null
        }
    }
}