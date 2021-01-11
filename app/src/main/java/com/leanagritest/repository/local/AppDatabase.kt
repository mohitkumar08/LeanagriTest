package com.leanagritest.repository.local

import android.content.Context
import androidx.room.*
import com.leanagritest.repository.local.dao.MoviesDao
import com.leanagritest.repository.local.entity.MovieModel
import java.util.Date

const val DATABASE_NAME = "leangiri.db"

@Database(
    entities = [MovieModel::class ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (null == INSTANCE) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, DATABASE_NAME
                        )
                            .addCallback(sRoomDatabaseCallback)
                            .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

        }


        fun clearDatabase() {
            INSTANCE?.clearAllTables()
        }
    }
    abstract fun moviesDao(): MoviesDao
}

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return (date?.time)
    }
}
