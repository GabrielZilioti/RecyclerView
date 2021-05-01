package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {

    private val list: ArrayList<Item> = ArrayList()

    private val adapter = RecyclerAdapter(list, this)

    var selectedPosition: Int = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<RecyclerView>(R.id.recycler_view)

        val button = findViewById<Button>(R.id.button)

        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this)
        view.setHasFixedSize(true)

        button.setOnClickListener {
            insertItemOrUpdateItem()
        }
    }

    override fun onItemClick(position: Int) {
        val selectedItem: Item = list[position]

        val title = findViewById<EditText>(R.id.edit_text_title)

        title.setText(selectedItem.text1)

        selectedPosition = position

        adapter.notifyItemChanged(position)

        Toast.makeText(this, "Editando item ${position + 1} (${selectedItem.text1})", Toast.LENGTH_SHORT).show()

        adapter.notifyItemChanged(position)
    }

    private fun insertItemOrUpdateItem() {

        val title = findViewById<EditText>(R.id.edit_text_title)

        if (title.text.isEmpty()) {
            Toast.makeText(this, "Você precisa adicionar um título para o item.", Toast.LENGTH_SHORT)
                .show()
        } else {
            val dateFormat = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy")

            dateFormat.timeZone = TimeZone.getTimeZone("GMT-3")

            val formatedDate = dateFormat.format(Date())

            if (selectedPosition != -1) {
                list[selectedPosition].text1 = title.text.toString()
                list[selectedPosition].text2 = formatedDate
                adapter.notifyItemChanged(selectedPosition)
                selectedPosition = -1
            } else {
                val newItemPosition = list.size + 1
                val newItem = Item(
                    R.mipmap.ite_logo_round,
                    title.text.toString(),
                    formatedDate
                )

                list.add(list.size, newItem)
                adapter.notifyItemInserted(list.size)
            }

            title.setText("")
        }
    }
}