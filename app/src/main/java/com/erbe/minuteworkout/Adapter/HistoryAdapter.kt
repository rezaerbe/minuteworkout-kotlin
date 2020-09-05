package com.erbe.minuteworkout.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erbe.minuteworkout.R
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(val context: Context, val items: ArrayList<String>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llHistory = view.history_item_main_ll
        val tvItem = view.date_item_tv
        val tvPosition = view.position_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(context)
            .inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val date : String = items.get(position)

        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date

        if (position % 2 == 0) {
            holder.llHistory.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }
        else {
            holder.llHistory.setBackgroundColor(Color.parseColor("FFF"))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}