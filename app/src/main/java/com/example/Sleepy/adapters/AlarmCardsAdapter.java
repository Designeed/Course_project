package com.example.Sleepy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Sleepy.R;

import java.util.List;

public class AlarmCardsAdapter extends RecyclerView.Adapter<AlarmCardsAdapter.ViewHolder> {
    private final List<AlarmCards> alarmCards;
    View view;

    public AlarmCardsAdapter(List<AlarmCards> cards) {
        this.alarmCards = cards;
    }

    @Override
    public AlarmCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_card_item, parent, false);
        return new AlarmCardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmCardsAdapter.ViewHolder holder, int position) {
        AlarmCards state = alarmCards.get(position);
        holder.titleView.setText(state.getTime());
        holder.remTime.setText(state.getRemainingTime());
    }

    @Override
    public int getItemCount() {
        return alarmCards.size();
    }

    public void delete(int position){
        alarmCards.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView, remTime;
        ViewHolder(View view){
            super(view);
            titleView = view.findViewById(R.id.tvTitleCard);
            remTime = view.findViewById(R.id.tvSecCard);
        }
    }
}
