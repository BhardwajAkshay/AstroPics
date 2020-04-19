package com.example.demoproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demoproject.modals.DatabaseItem


@Database(entities = [DatabaseItem::class], version = 1)
abstract class database: RoomDatabase(){
    abstract fun dao(): DAO
}