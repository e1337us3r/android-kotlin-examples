package com.apolets.sqliteapp

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = [(ForeignKey(
        entity = User::class,
        parentColumns = ["uid"],
        childColumns = ["user"]))])
data class ContactInfo(
        val user: Int,
        val type: String, //e.g. phone, fb, twitter,...
        @PrimaryKey
        val value: String)