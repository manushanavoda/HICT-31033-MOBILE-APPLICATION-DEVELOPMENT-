package com.example.uidesignlab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HeaderFragment extends Fragment {

    private static final String TAG = "LifecycleTracker";

    public HeaderFragment() {
        // Empty constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        Log.d(TAG, "Header onAttach");
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "Header onCreateView");

        return inflater.inflate(
                R.layout.fragment_header,
                container,
                false
        );
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        Log.d(TAG, "Header onDestroyView");
    }
}