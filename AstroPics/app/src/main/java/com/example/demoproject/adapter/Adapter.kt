package com.example.demoproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.ClickListener
import com.example.demoproject.R
import com.example.demoproject.database.database
import com.example.demoproject.modals.DatabaseItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.saved_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class adapter(private val item: List<DatabaseItem>,
              private val context: Context,
              private val db: database,
              private val clicker: ClickListener
             ) : RecyclerView.Adapter<adapter.Holder>() {

    override fun onCreateViewHolder(viewgroup: ViewGroup, p: Int): Holder {
        val layout = LayoutInflater.from(context).inflate(R.layout.saved_item, viewgroup, false)
        return Holder(layout)
    }

    override fun getItemCount() = item.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition))

        val list = db.dao().getAll()

        with(holder){
            title.text   = list[position].title
            content.text = list[position].content
        }

        if(!list[position].url.isEmpty())
        Picasso.with(context)
            .load(list[position].url)
            .placeholder(R.drawable.ic_loading_icon)
            .error(R.drawable.ic_error_loading_icon)
            .resize(64,64)
            .centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener{
            clicker.Click(list[position])
        }

        holder.itemView.setOnLongClickListener{
            clicker.LongClick(list[position])
            //notifyItemRemoved(position)
            //notifyDataSetChanged()
            return@setOnLongClickListener true
        }
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val title   = view.savedItemTitle!!
        val content = view.savedItemContent!!
        val image   = view.savedItemImage!!
    }
}