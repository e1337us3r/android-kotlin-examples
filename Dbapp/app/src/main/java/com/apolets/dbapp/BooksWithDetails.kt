package com.apolets.dbapp

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import android.util.Log

class BooksWithDetails(
        @Embedded
        val book: Book

){
    @Relation(parentColumn = "uid",entityColumn = "book", entity = BookDetail::class)
    lateinit var details : List<BookDetail>

    override fun toString(): String {
        return "$book ${buildListString()}"
    }

    fun buildListString():String{
        var result : String = "Copies: "

        details.forEach {
            result += it.toString()+","
        }
        return result.substring(0,result.length-1)
    }
}