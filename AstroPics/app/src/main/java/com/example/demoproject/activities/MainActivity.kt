package com.example.demoproject.activities

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.room.Room
import com.example.demoproject.R
import com.example.demoproject.database.database
import com.example.demoproject.modals.DatabaseItem
import com.example.demoproject.modals.Modal
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.imageView
import kotlinx.android.synthetic.main.activity_main.tvDate
import kotlinx.android.synthetic.main.activity_main.tvExplanation
import kotlinx.android.synthetic.main.activity_main.tvName
import kotlinx.android.synthetic.main.activity_main.tvTitle
import kotlinx.android.synthetic.main.sudo.*
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : androidx.appcompat.app.AppCompatActivity() {

    lateinit var by: String
    lateinit var title: String
    lateinit var content: String
    lateinit var urlToPass: String
    lateinit var date: String
    lateinit var mediaType: String
    lateinit var img: Bitmap
    var rawJson = "Link no longer available. Visit  https://apod.nasa.gov/apod/archivepix.html  for older links."

    private lateinit var db: database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sudo)

        db = Room.databaseBuilder(
            this,
            database::class.java
            , "Database.db"
        ).allowMainThreadQueries().build()

        title = intent.getStringExtra("ttl") ?: "null"
        content = intent.getStringExtra("cnt") ?: "null"
        urlToPass = intent.getStringExtra("utp") ?: ""
        date = intent.getStringExtra("dt") ?: ""
        mediaType = intent.getStringExtra("mt") ?: ""
        by = intent.getStringExtra("by") ?: ""

        if (title == "null" && content == "null") {

            fetchOnline()
        } else putOnScreen(title, content, urlToPass, date, mediaType, by)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        setWallpaper.setOnClickListener {

            if (mediaType == "image") {

                val bckgrnd = object : Thread() {
                    override fun run() {
                        try {
                            img = Picasso.with(baseContext).load(urlToPass).get()

                            val dm = DisplayMetrics()
                            windowManager.defaultDisplay.getMetrics(dm)

                            val h: Int = dm.heightPixels
                            val w: Int = dm.widthPixels

                            img = wllppr(img)

                            val wallpprMngr = WallpaperManager.getInstance(baseContext)

                            try {
                                wallpprMngr.setBitmap(img)
                                wallpprMngr.suggestDesiredDimensions(w, h)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                bckgrnd.start()
                Toast.makeText(baseContext, "Wallpaper Changed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(baseContext, "Wallpaper can't be changed", Toast.LENGTH_SHORT).show()
            }
        }

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun putOnScreen(t: String, c: String, u: String, d: String, mT: String, by: String) {

        tvTitle.text = t

        tvExplanation.visibility = View.VISIBLE
        tvExplanation.text = c

        tvDate.visibility = View.VISIBLE
        tvDate.text = "On $d"

        tvName.visibility = View.VISIBLE
        tvName.text = "By $by"

        if (mT != "video") {

            imageView.visibility = View.VISIBLE

            Picasso.with(this@MainActivity)
                .load(u)
                .placeholder(R.drawable.ic_loading_icon)
                .error(R.drawable.ic_error_loading_icon)
                .into(imageView)
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun fetchOnline() {

        val url = "https://api.nasa.gov/planetary/apod?api_key=${applicationContext.resources.getString(R.string.api_key)}"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                runOnUiThread {
                    tvTitle.text = "Failed to load :/"
                    //call.enqueue(this)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                rawJson = body!!

                Log.e("raw json", "\n\n" + body + "\n\n")

                val gson = GsonBuilder().create()

                val modal = gson.fromJson(body, Modal::class.java)

                by = modal.copyright ?: "_"
                title = modal.title ?: "_"
                content = modal.explanation
                urlToPass = modal.url
                date = modal.date.toString()
                mediaType = modal.media_type

                runOnUiThread {
                    putOnScreen(title, content, urlToPass, date, mediaType, by)
                }
            }
        })

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun isOnline(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(mItem: MenuItem?): Boolean {

        when (mItem?.itemId) {

            R.id.download -> {

                //if connected, download
                if (isOnline(this)) {
                    val item = DatabaseItem(0, title, content, urlToPass, date, mediaType, by)

                    if (db.dao().chkIfPresent(title) != title) {
                        Toast.makeText(baseContext, "Downloaded", Toast.LENGTH_SHORT).show()
                        db.dao().insert(item)
                    } else {
                        Toast.makeText(baseContext, "already exists", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Log.e("TAG", "Not connected to internet")
                }
                return true
            }

            R.id.saved -> {
                val i = Intent(this, NewActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                return true
            }

            R.id.about -> {
                val i = Intent(this, About::class.java)
                startActivity(i)
                return true
            }

            R.id.home -> {
                val i = Intent(this, MainActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                return true
            }

            R.id.media -> {
                val i = Intent(this, Media::class.java)
                i.putExtra("raw", rawJson)
                startActivity(i)
                return true
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("title", title)
        outState.putString("content", content)
        outState.putString("date", date)
        outState.putString("url", urlToPass)
        outState.putString("mt", mediaType)
        outState.putString("by", by)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        title = savedInstanceState?.getString("title") ?: "Title"
        date = savedInstanceState?.getString("date") ?: ""
        content = savedInstanceState?.getString("content") ?: ""
        urlToPass = savedInstanceState?.getString("url") ?: ""
        mediaType = savedInstanceState?.getString("mt") ?: ""
        by = savedInstanceState?.getString("by") ?: ""

        putOnScreen(title, content, urlToPass, date, mediaType, by)
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //cut/expand extra pixels to the image in comparision to screen resolution
    fun wllppr(bitmap: Bitmap): Bitmap{

        val display = windowManager.defaultDisplay

        val bitmapH = bitmap.height
        val bitmapW = bitmap.width
        val screenH = display.height
        val screenW = display.width

        val bitmapRatio   = bitmapW/bitmapH
        val screenRatio = screenW/screenH

        val newBitmapH: Int
        val newBitmapW: Int

        if(screenRatio > bitmapRatio){
            newBitmapW = screenW
            newBitmapH = newBitmapW/bitmapRatio
        }else{
            newBitmapH = screenH
            newBitmapW = newBitmapH * bitmapRatio
        }

        var newBitmap = Bitmap.createScaledBitmap(bitmap, newBitmapW, newBitmapH, true)

        val bitmapGapX: Int = (newBitmapW - screenW)/2
        val bitmapGapY: Int = (newBitmapH - screenH)/2

        newBitmap = Bitmap.createBitmap(newBitmap, bitmapGapX, bitmapGapY, screenW, screenH)

        return newBitmap
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
