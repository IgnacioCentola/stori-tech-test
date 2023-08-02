package com.nacho.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.nacho.data.repository.DataStoreRepositoryImpl
import com.nacho.domain.repository.StoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.datastore.preferences.core.Preferences as DataStorePreferences

private const val PREFERENCES_NAME = "my_preferences"
val Context.dataStore: DataStore<DataStorePreferences> by preferencesDataStore(name = PREFERENCES_NAME)

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<DataStorePreferences> {
        return applicationContext.dataStore
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(dataStore: DataStore<DataStorePreferences>): StoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }

}