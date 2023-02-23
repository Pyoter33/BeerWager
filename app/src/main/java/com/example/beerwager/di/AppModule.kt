package com.example.beerwager.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.beerwager.data.data_source.Wager
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
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context, provider: Provider<WagerDao>): WagersDatabase {
        return Room.databaseBuilder(context, WagersDatabase::class.java, WagersDatabase.NAME)
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                            val dao = provider.get()
                            dao.createWager(Wager(
                                "Wager 1",
                                "description description description description",
                                LocalDate.of(2023, 2, 15),
                                LocalTime.of(10, 10),
                                0,
                                listOf("Wagerer 1", "Wagerer 2", "Wagerer 3"),
                                4,
                                false,
                                true
                            ))
                            dao.createWager(Wager(
                                "Wager 2",
                                "description",
                                LocalDate.of(2022, 10, 15),
                                null,
                                1,
                                listOf("Wagerer 1"),
                                10,
                                true,
                                false
                            ))
                            dao.createWager(Wager(
                                "Wager 3",
                                "description description description description",
                                LocalDate.of(2023, 2, 15),
                                LocalTime.of(10, 10),
                                2,
                                listOf("Wagerer 1", "Wagerer 2", "Wagerer 3"),
                                4,
                                false,
                                false
                            ))
                            dao.createWager(Wager(
                                "Wager 4",
                                "description description description description",
                                LocalDate.of(2023, 2, 28),
                                LocalTime.of(10, 10),
                                3,
                                listOf("Wagerer 1", "Wagerer 2", "Wagerer 3"),
                                4,
                                true,
                                false
                            ))
                        }
                    }
                }).build()
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

}