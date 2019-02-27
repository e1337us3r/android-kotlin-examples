package com.apolets.dbapp

import android.arch.persistence.room.*

@Entity(tableName = "book")
data class Book(
        @PrimaryKey(autoGenerate = true)
        val uid: Int,
        val name: String,
        val description: String

){
    override fun toString(): String = "$uid $name $description"
}

@Entity(tableName = "bookDetail",foreignKeys = [(ForeignKey(entity = Book::class, parentColumns = ["uid"],childColumns = ["book"]))])
data class BookDetail(
        val book: Int,
        val library: String,
        @PrimaryKey
        val code: String
){
    override fun toString(): String =  "$library $code"

}
