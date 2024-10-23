package com.example.utils

import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.experimental.and

fun generateSalt(): ByteArray {
    val salt = ByteArray(16) // 128-bit salt
    SecureRandom().nextBytes(salt)
    return salt
}

fun hash(password: String, salt: ByteArray): ByteArray {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(salt)
    return md.digest(password.toByteArray(Charsets.UTF_8))
}

fun ByteArray.toHexString(): String {
    return joinToString("") { String.format("%02x", it and 0xff.toByte()) }
}

fun hashedPassword(password: String) : String{
    val salt = generateSalt()

    val hashedPassword = hash(password = password,salt = salt)

    return hashedPassword.toHexString()
}