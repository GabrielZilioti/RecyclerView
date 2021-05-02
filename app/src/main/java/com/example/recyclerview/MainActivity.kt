package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener,
    RecyclerAdapter.OnLongCLickListener {

    private val list: ArrayList<Item> = ArrayList()

    private val adapter = RecyclerAdapter(list, this, this)

    var selectedPosition: Int = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<RecyclerView>(R.id.recycler_view)

        val button = findViewById<Button>(R.id.button)

        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this)
        view.setHasFixedSize(true)

        button.setOnClickListener {
            insertItemOrUpdateItem()
        }

        swipeRefresh.setOnRefreshListener {
            invertAndReorganizeItems()
        }
    }

    override fun onItemClick(position: Int) {
        val selectedItem: Item = list[position]

        val title = findViewById<EditText>(R.id.edit_text_title)

        title.setText(selectedItem.text1)

        selectedPosition = position

        adapter.notifyItemChanged(position)

        showToast("Editando item ${position + 1} (${selectedItem.text1})")

        adapter.notifyItemChanged(position)
    }

    override fun onLongClick(position: Int) {
        deleteItem(position)
    }

    private fun deleteItem(position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Deseja deletar o item ${list[position].text1} ?")
            .setMessage("Está ação não pode ser revertida")
            .setNeutralButton("Cancelar") { _, _ ->
                print("Ação cancelada pelo usuário")
            }
            .setPositiveButton("Deletar") { _, _ ->
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
            .show()
    }

    private fun invertAndReorganizeItems() {
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        Timer("Waiting", false).schedule(2000) {
            if (list.size >= 1) {

                list.forEach {
                    it.text1 = it.text1.reversed()
                    it.text2 = it.text2.reversed()
                }

                reorganizeList()

                adapter.notifyItemRangeChanged(0, list.size)
            } else {
                showToast("Não há items para serem atualizados")
            }
            swipeRefresh.isRefreshing = false
        }
    }

    private fun insertItemOrUpdateItem() {

        val title = findViewById<EditText>(R.id.edit_text_title)

        if (title.text.isEmpty()) {
            showToast("Você precisa adicionar um título para o item.")
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
                list.add(list.size, Item(
                    R.mipmap.ite_logo_round,
                    title.text.toString(),
                    formatedDate
                ))
                adapter.notifyItemInserted(list.size)
            }
            title.setText("")
        }
    }

    private fun reorganizeList() {
        val lastItem = list[list.size - 1]

        list.removeAt(list.size - 1)
        list.add(0, lastItem)
    }

    private fun showToast(message: String) {
        Snackbar.make(
            findViewById(R.id.relative_view),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}