package com.example.demoproject

import com.example.demoproject.modals.DatabaseItem

interface ClickListener{

    fun Click(item : DatabaseItem)
    fun LongClick(item: DatabaseItem)

}