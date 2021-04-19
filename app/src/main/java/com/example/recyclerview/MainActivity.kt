package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val list: ArrayList<Item> = ArrayList()

    private val adapter = RecyclerAdapter(list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<RecyclerView>(R.id.recycler_view)

        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this)
        view.setHasFixedSize(true)
    }

    fun insertItem(view: View) {

        val title = findViewById<EditText>(R.id.edit_text_title)

        if (title.text.isEmpty()) {
            Toast.makeText(this, "Você precisa adicionar um título para o item.", Toast.LENGTH_SHORT)
                .show()
        } else {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

            dateFormat.timeZone = TimeZone.getTimeZone("GMT-3")

            val newItem = Item(
                R.mipmap.ite_logo_round,
                title.text.toString(),
                dateFormat.format(Date())
            )

            list.add(list.size, newItem)
            adapter.notifyItemInserted(list.size)
        }
    }

    fun removeLast(view: View) {
        if (list.isEmpty()) {
            Toast.makeText(this, "Não há items para remover.", Toast.LENGTH_SHORT)
                .show()
        } else {
            list.removeAt(list.size - 1)
            adapter.notifyItemRemoved(list.size)
        }
    }
}