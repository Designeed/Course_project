package com.example.Sleepy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.Sleepy.R;
import com.example.Sleepy.shared.MyAlarm;

import java.util.List;

public class WakeCardsAdapter extends RecyclerView.Adapter<WakeCardsAdapter.ViewHolder> {
    private final List<WakeCards> alarmCards;
    View view;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public WakeCardsAdapter(List<WakeCards> cards) {
        this.alarmCards = cards;
    }

    @NonNull
    @Override
    public WakeCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_wake, parent, false);
        return new WakeCardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WakeCardsAdapter.ViewHolder holder, int position) {
        WakeCards state = alarmCards.get(position);
        holder.titleView.setText(state.getTime());
        holder.remTime.setText(state.getRemainingTime());

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.srlCard, state.getTime());

        holder.ivAddAlarm.setOnClickListener(view1 -> MyAlarm.setAlarm(view.getContext(), state.getTriggerTime(), view1));
        //Toast.makeText(view.getContext(), "" + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(state.getTriggerTime()), Toast.LENGTH_SHORT).show());
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
        final ImageView ivAddAlarm;
        final SwipeRevealLayout srlCard;

        ViewHolder(View view){
            super(view);
            titleView = view.findViewById(R.id.tvTitleCard);
            remTime = view.findViewById(R.id.tvSecCard);
            ivAddAlarm = view.findViewById(R.id.ibAddAlarm);
            srlCard = view.findViewById(R.id.srlCard);
        }
    }
}
