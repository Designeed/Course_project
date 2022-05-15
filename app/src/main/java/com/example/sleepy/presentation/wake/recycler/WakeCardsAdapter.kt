package com.example.sleepy.presentation.wake.recycler

import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.sleepy.R
import android.widget.TextView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.example.sleepy.utils.MyAlarm

class WakeCardsAdapter(private val alarmCards: ArrayList<WakeCards>) :
    RecyclerView.Adapter<WakeCardsAdapter.ViewHolder>() {
    private lateinit var view: View
    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_wake, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state = alarmCards[position]
        holder.titleView.text = state.time
        holder.remTime.text = state.remainingTime
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(holder.srlCard, state.time)

        holder.ivAddAlarm.setOnClickListener { view1: View? ->
            MyAlarm.setAlarm(view.context, state.getTriggerTime(), view1!!)
        }
        //Toast.makeText(view.getContext(), "" + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(state.getTriggerTime()), Toast.LENGTH_SHORT).show());
    }

    override fun getItemCount(): Int {
        return alarmCards.size
    }

    fun delete(position: Int) {
        alarmCards.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(
        view
    ) {
        val titleView: TextView = view.findViewById(R.id.tvTitleCard)
        val remTime: TextView = view.findViewById(R.id.tvSecCard)
        val ivAddAlarm: ImageView = view.findViewById(R.id.ibAddAlarm)
        val srlCard: SwipeRevealLayout = view.findViewById(R.id.srlCard)
    }
}