package com.nacho.model


data class User(
    val userName: String? = null,
    val age: String? = null,
    val email: String? = null,
    val idUrl: String = "",
    val movements: List<Movement> = emptyList()
)
