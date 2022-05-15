package com.example.sleepy.presentation.onBoarding.viewpager

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.sleepy.R
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class OnBoardingAdapter(private val boardings: ArrayList<OnBoarding>) :
    RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_container_on_boarding, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state = boardings[position]
        holder.titleView.text = state.title
        holder.descText.text = state.description
        holder.lottie.setImageResource(state.image)
    }

    override fun getItemCount(): Int {
        return boardings.size
    }

    fun delete(position: Int) {
        boardings.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(
        view
    ) {
        val titleView: TextView = view.findViewById(R.id.tvTitleBoard)
        val descText: TextView = view.findViewById(R.id.tvDescriptionBoard)
        val lottie: LottieAnimationView = view.findViewById(R.id.lottieSrcBoard)
    }
}