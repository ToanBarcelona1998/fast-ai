package com.example.utils

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(password: String): String {
    // Generate a salt and hash the password
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
    // Check if the password matches the hash
    return BCrypt.checkpw(plainPassword, hashedPassword)
}