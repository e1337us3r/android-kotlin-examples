package com.apolets.sqliteapp

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey
        val uid: Int,
        val firstname: String,
        val lastname: String,
        @Embedded
        val contacts: List<ContactInfo>

){
    override fun toString(): String = "First and last name = $firstname $lastname"
}

