package com.example.Sleepy.ui.wake;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.Sleepy.databinding.FragmentWakeBinding;

public class WakeFragment extends Fragment {

    private WakeViewModel wakeViewModel;
    private FragmentWakeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) { wakeViewModel =
                new ViewModelProvider(this).get(WakeViewModel.class);

    binding = FragmentWakeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.tvSetTime;
        wakeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}