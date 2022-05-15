package com.example.sleepy.presentation.sleep.recycler

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.sleepy.R
import android.widget.TextView

class SleepCardsAdapter(private val sleepCards: ArrayList<SleepCards>) :
    RecyclerView.Adapter<SleepCardsAdapter.ViewHolder>() {
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_sleep, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeCards = sleepCards[position]
        holder.titleView.text = timeCards.title
        holder.secText.text = timeCards.secText
        //MyAnimator.setScaleAnimation(holder.itemView);
    }

    override fun getItemCount(): Int {
        return sleepCards.size
    }

    fun delete(position: Int) {
        sleepCards.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(
        view
    ) {
        val titleView: TextView = view.findViewById(R.id.tvTitleCard)
        val secText: TextView = view.findViewById(R.id.tvSecCard)

    }
}