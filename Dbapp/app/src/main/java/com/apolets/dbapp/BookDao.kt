package com.apolets.dbapp

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface BookDao {

    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: Book)

    @Update
    fun update(book: Book)

    @Query("SELECT * FROM book")
    fun getAllBooksWithDetails() : List<BooksWithDetails>

}

@Dao
interface BookDetailsDao{

    @Query("SELECT * FROM bookDetail")
    fun getAll(): List<BookDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookDetail: BookDetail)

}