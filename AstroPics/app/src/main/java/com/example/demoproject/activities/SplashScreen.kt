package com.example.demoproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.demoproject.R
import kotlinx.android.synthetic.main.splash_screen.*
import java.lang.Exception

class SplashScreen : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val animation_ : Animation = AnimationUtils.loadAnimation(baseContext, R.anim.splash_transition)
        splashScreenImage.startAnimation(animation_)

        val bckgrnd = object : Thread(){
            override fun run() {
                try {
                    Thread.sleep(3000)

                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

        bckgrnd.start()

    }
}