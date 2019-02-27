package com.apolets.sqliteapp

import android.arch.persistence.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user " +
            "INNER JOIN contactinfo " +
            "ON user.uid = contactinfo.user " +
            "WHERE user.uid = :userid")
    fun getUserContacts(userid: Int): User
}

@Dao
interface ContactInfoDao{

    @Insert()
    fun insert(user: User)

    @Query("SELECT * FROM contactinfo")
    fun getAll(): List<User>

    @Update
    fun update(user: User)
}