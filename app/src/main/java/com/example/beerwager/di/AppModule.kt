package com.example.beerwager.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.beerwager.data.data_source.WagerDao
import com.example.beerwager.data.data_source.WagersDatabase
import com.example.beerwager.data.repository.WagerRepositoryImpl
import com.example.beerwager.domain.repository.WagerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WagersDatabase {
        return Room.databaseBuilder(context, WagersDatabase::class.java, WagersDatabase.NAME).build()
    }

    @Provides
    @Singleton
    fun provideDao(database: WagersDatabase): WagerDao {
        return database.wagerDao
    }

    @Provides
    @Singleton
    fun provideRepository(wagerDao: WagerDao): WagerRepository {
        return WagerRepositoryImpl(wagerDao)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

}