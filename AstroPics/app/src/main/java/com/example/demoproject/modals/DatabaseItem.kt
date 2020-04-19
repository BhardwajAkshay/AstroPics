package com.example.demoproject.modals

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class DatabaseItem(
    @PrimaryKey(autoGenerate = true)
    val key: Long=-1,

    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "mediaType")
    val mediaType: String,
    @ColumnInfo(name = "by")
    val by: String
)