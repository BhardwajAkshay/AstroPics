package com.example.demoproject.database

import androidx.room.*
import com.example.demoproject.modals.DatabaseItem


@Dao
interface DAO{

    @Insert
    fun insert(item: DatabaseItem): Long

    @Delete
    fun delete(item: DatabaseItem)

    @Update
    fun update(item: DatabaseItem)

    @Query("SELECT * FROM DatabaseItem")
    fun getAll():List<DatabaseItem>

    @Query("SELECT title FROM DatabaseItem WHERE title=:title")
    fun chkIfPresent(title: String): String //Try returning a boolean value instead of returning a full string
}