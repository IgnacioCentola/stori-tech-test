package com.nacho.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val userName: String? = null,
    val age: String? = null,
    val email: String? = null,
    val idUrl: String? = null,
    val movements: List<Movement>? = null
)
