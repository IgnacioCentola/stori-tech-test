package com.nacho.model

import com.google.firebase.Timestamp
import java.util.Date


data class Movement(
    val id: String? = null,
    val amount: Int = 0,
    val date: Timestamp = Timestamp(Date()),
    val receiver: String?= null
)
