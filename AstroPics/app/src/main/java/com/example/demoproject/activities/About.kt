package com.example.demoproject.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demoproject.R

class About : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about)

        supportActionBar?.setTitle("About")
    }
}