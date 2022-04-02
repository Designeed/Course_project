package com.example.Sleepy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Sleepy.R;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.ViewHolder> {
    private final List<OnBoarding> boardings;
    View view;

    public OnBoardingAdapter(List<OnBoarding> states) {
        this.boardings = states;
    }

    @Override
    public OnBoardingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_onboarding, parent, false);
        return new OnBoardingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OnBoardingAdapter.ViewHolder holder, int position) {
        OnBoarding state = boardings.get(position);
        holder.titleView.setText(state.getTitle());
        holder.descText.setText(state.getDescription());
        holder.lottie.setImageResource(state.getImage());
    }

    @Override
    public int getItemCount() {
        return boardings.size();
    }

    public void delete(int position){
        boardings.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView, descText;
        final LottieAnimationView lottie;
        ViewHolder(View view){
            super(view);
            titleView = view.findViewById(R.id.tvTitleBoard);
            descText = view.findViewById(R.id.tvDescriptionBoard);
            lottie = view.findViewById(R.id.lottieSrcBoard);
        }
    }
}
