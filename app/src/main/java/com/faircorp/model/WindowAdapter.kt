package com.faircorp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faircorp.R

class WindowAdapter(val listener: OnWindowSelectedListener) : RecyclerView.Adapter<WindowAdapter.WindowViewHolder>() { // (1)

    inner class WindowViewHolder(view: View) : RecyclerView.ViewHolder(view) { // (2)
        val name: TextView = view.findViewById(R.id.txt_item_window_name)
        val room: TextView = view.findViewById(R.id.txt_item_window_room_name)
        val status: TextView = view.findViewById(R.id.txt_item_window_status)
    }

    private val items = mutableListOf<WindowDto>() // (3)

    fun update(windows: List<WindowDto>) {  // (4)
        items.clear()
        items.addAll(windows)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size // (5)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindowViewHolder { // (6)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_windows_item, parent, false)
        return WindowViewHolder(view)
    }

    override fun onBindViewHolder(holder: WindowViewHolder, position: Int) {  // (7)
        val window = items[position]
        holder.apply {
            name.text = window.name
            status.text = window.windowStatus.toString()
            room.text = window.roomName
            itemView.setOnClickListener {listener.onWindowSelected((window.id))}
        }
    }
    override fun onViewRecycled(holder: WindowViewHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            itemView.setOnClickListener(null)
        }
    }
}