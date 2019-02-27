package com.apolets.dbapp

import android.arch.persistence.room.*
import android.content.Context


@Database(entities = [(Book::class),(BookDetail::class)], version = 4)
abstract class LibDB : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun bookDetailsDao(): BookDetailsDao


    companion object {
        private var sInstance: LibDB? = null
        @Synchronized
        fun get(context: Context): LibDB {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context.applicationContext,
                        LibDB::class.java, "lib.db").build()
            }
            return sInstance!!
        }
    }
}