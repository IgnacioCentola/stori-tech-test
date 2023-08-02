package com.nacho.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.nacho.data.repository.UserRepositoryImpl
import com.nacho.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

//    @Singleton
//    @Provides
//    fun provideContentResolver(@ApplicationContext context: Context) = context.contentResolver

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirestoreDatabaseReference() = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirestoreRealtimeDatabaseRef() = Firebase.database.reference

    @Singleton
    @Provides
    fun provideFirebaseStorage() = Firebase.storage.reference

    @Provides
    fun provideUserRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        databaseReference: DatabaseReference,
        firebaseStorage: StorageReference
    ): UserRepository =
        UserRepositoryImpl(
            auth = auth,
            firestore = firestore,
            database = databaseReference,
            storageRef = firebaseStorage
        )
}