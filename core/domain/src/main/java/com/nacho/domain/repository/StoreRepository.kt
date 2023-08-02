package com.nacho.domain.repository

interface StoreRepository {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
}