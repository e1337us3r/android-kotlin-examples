package com.apolets.sqliteapp

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

@Database(entities = [(User::class), (ContactInfo::class)],
        version = 1)
@TypeConverters(Converter::class)
abstract class UserDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun contactDao(): ContactInfoDao
    /* one and only one instance */
    companion object {
        private var sInstance: UserDB? = null
        @Synchronized
        fun get(context: Context): UserDB {
            if (sInstance == null) {
                sInstance =
                        Room.databaseBuilder(context.applicationContext,
                                UserDB::class.java, "user.db").build()
            }
            return sInstance!!
        }
    }
}