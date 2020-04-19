package com.example.demoproject.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.demoproject.ClickListener
import com.example.demoproject.R
import com.example.demoproject.adapter.adapter
import com.example.demoproject.database.database
import com.example.demoproject.modals.DatabaseItem
import kotlinx.android.synthetic.main.saved_data.*
import java.text.FieldPosition

class NewActivity : AppCompatActivity(), ClickListener {

    private lateinit var db   : database
    private lateinit var list : List<DatabaseItem>
    private lateinit var _adapter: adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_data)

        db = Room.databaseBuilder(this,
            database::class.java
            ,"Database.db").allowMainThreadQueries().build()

        list = db.dao().getAll()

        _adapter = adapter(list,this,db,this)

        savedItemView.adapter = _adapter
        savedItemView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
    }

    override fun Click(item: DatabaseItem) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("ttl",item.title)
        intent.putExtra("dt",item.date)
        intent.putExtra("cnt",item.content)
        intent.putExtra("utp",item.url)
        intent.putExtra("mt",item.mediaType)
        intent.putExtra("by",item.by)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
    }

    override fun LongClick(item: DatabaseItem) {
        db.dao().delete(item)
        //db.dao().update(item)
        //_adapter.notifyDataSetChanged()
        Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show()
    }
}