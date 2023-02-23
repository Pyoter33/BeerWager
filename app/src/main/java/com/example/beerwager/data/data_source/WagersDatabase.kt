package com.example.beerwager.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.beerwager.utils.Converters


@Database(
    entities = [Wager::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WagersDatabase: RoomDatabase() {

    abstract val wagerDao: WagerDao

    companion object {
        const val NAME = "db"
    }

}