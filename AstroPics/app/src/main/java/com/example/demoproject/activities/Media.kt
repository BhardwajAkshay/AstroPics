package com.example.demoproject.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.demoproject.R
import kotlinx.android.synthetic.main.media.*

class Media : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media)

        supportActionBar?.setTitle("Media")

        rawJSON.text = intent.getStringExtra("raw")

    }
}