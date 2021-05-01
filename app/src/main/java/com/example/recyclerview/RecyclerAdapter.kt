package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    private val list: List<Item>,
    private val listener: OnItemClickListener,
    private val longListener: OnLongCLickListener
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2
        "# ${position + 1}".also { holder.textView3.text = it }
        holder.imageView.setImageResource(currentItem.imageResource)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener, View.OnLongClickListener{
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textView1: TextView = itemView.findViewById(R.id.text_view_1)
        val textView2: TextView = itemView.findViewById(R.id.text_view_2)
        val textView3: TextView = itemView.findViewById(R.id.text_view_3)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                longListener.onLongClick(position)
                return true
            }
            return false
        }
    }

    interface OnLongCLickListener {
        fun onLongClick(position: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}