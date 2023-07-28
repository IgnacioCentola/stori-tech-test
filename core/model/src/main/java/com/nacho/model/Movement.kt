package com.nacho.model

data class Movement(
    val id: String? = null,
    val amount: Int = 0,
    val date: Long = 0L,
    val receiver: String?= null
)
