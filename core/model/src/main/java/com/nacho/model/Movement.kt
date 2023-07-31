package com.nacho.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Movement(
    val id: String? = null,
    val amount: Int? = null,
    val date: Timestamp? = null,
//    val date: Timestamp? = null,
    val receiver: String? = null
)
