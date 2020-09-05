package com.erbe.minuteworkout.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.erbe.minuteworkout.Model.ExerciseModel
import com.erbe.minuteworkout.R
import kotlinx.android.synthetic.main.item_exercise.view.*

class ExerciseAdapter(val items: ArrayList<ExerciseModel>, val context: Context) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem = view.item_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(context)
            .inflate(R.layout.item_exercise, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model : ExerciseModel = items[position]

        holder.tvItem.text = model.getId().toString()

        if(model.getIsSelected()) {
            holder.tvItem.background = ContextCompat.getDrawable(context,R.drawable.item_circular_thin_color_border)
            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        }
        else if(model.getIsCompleted()) {
            holder.tvItem.background = ContextCompat.getDrawable(context,R.drawable.circular_color_background)
            holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
        }
        else {
            holder.tvItem.background = ContextCompat.getDrawable(context,R.drawable.item_circular_color_gray_bg)
            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}