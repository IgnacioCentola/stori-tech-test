package com.nacho.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nacho.data.repository.UserRepository
import com.nacho.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirestoreDatabaseReference() = Firebase.database.reference

    @Provides
    fun provideUserRepository(auth: FirebaseAuth, database: DatabaseReference) : UserRepository =
        UserRepositoryImpl(auth, database)
}