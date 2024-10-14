package com.masterandroid.timerapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.masterandroid.timerapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentTimeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Chronometer chronometer;
    private Handler handler = new Handler();
    private Runnable runnable;

    public CurrentTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentTimeFragment newInstance(String param1, String param2) {
        CurrentTimeFragment fragment = new CurrentTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_time, container, false);
        chronometer = view.findViewById(R.id.chronometerCurrentTime);

        startUpdatingTime();

        return view;
    }

    private void startUpdatingTime() {
        runnable = new Runnable() {
            @Override
            public void run() {
                displayCurrentTime();
                handler.postDelayed(this, 1000); // Cập nhật mỗi giây
            }
        };
        handler.post(runnable); // Bắt đầu cập nhật thời gian
    }

    private void displayCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        chronometer.setText(currentTime);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Dừng cập nhật khi Fragment bị hủy
    }
}