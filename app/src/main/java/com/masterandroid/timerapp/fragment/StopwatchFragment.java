package com.masterandroid.timerapp.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.masterandroid.timerapp.R;
import com.masterandroid.timerapp.adapter.Stopwatch_RecyclerViewAdapter;
import com.masterandroid.timerapp.database.DatabaseHandler;
import com.masterandroid.timerapp.model.StopwatchModel;

import java.util.ArrayList;

public class StopwatchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button startButton;
    private Button stopButton;
    private Chronometer chronometer;
    private boolean isClickStart = false;
    private long timeWhenStop = 0;
    private long timeCircle = 0;
    private int count = 1;

    private ArrayList<StopwatchModel> stopwatchModels;

    public StopwatchFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StopwatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StopwatchFragment newInstance(String param1, String param2) {
        StopwatchFragment fragment = new StopwatchFragment();
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
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        startButton = view.findViewById(R.id.btnStart);
        stopButton = view.findViewById(R.id.btnStop);
        chronometer = view.findViewById(R.id.chronometerStopwatch);
        startButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        stopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        stopwatchModels = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewStopwatch);
        Stopwatch_RecyclerViewAdapter recyclerViewAdapter = new Stopwatch_RecyclerViewAdapter(getContext(), stopwatchModels);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClickStart = !isClickStart;
                if (isClickStart) {
                    startButton.setBackgroundColor(Color.parseColor("#340e0b"));
                    startButton.setTextColor(Color.parseColor("#eb4d48"));

                    timeCircle = SystemClock.elapsedRealtime();

                    chronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStop);
                    chronometer.start();

                    startButton.setText("Dừng");
                    stopButton.setText("Vòng");
                } else {
                    startButton.setBackgroundColor(Color.parseColor("#092911"));
                    startButton.setTextColor(Color.parseColor("#26bc4b"));

                    timeWhenStop = SystemClock.elapsedRealtime() - chronometer.getBase();
                    timeCircle = timeWhenStop;

                    chronometer.stop();

                    startButton.setText("Bắt đầu");
                    stopButton.setText("Đặt lại");
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopButton.getText().equals("Vòng") && startButton.getText().equals("Dừng")) {
                    long timePassed = SystemClock.elapsedRealtime() - timeCircle;
                    timeCircle = SystemClock.elapsedRealtime();

                    String time = getFormattedTime(timePassed);
                    recyclerViewAdapter.addItem(new StopwatchModel(count, time));
                    count++;
                } else if (stopButton.getText().equals("Đặt lại")){
                    stopwatchModels.clear();
                    recyclerViewAdapter.notifyDataSetChanged();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timeCircle = 0;
                    timeWhenStop = 0;
                    count = 1;
                }
            }
        });

        return view;
    }

    private String getFormattedTime(long millis) {
        int hours = (int) (millis / 3600000);
        int minutes = (int) (millis % 3600000) / 60000;
        int seconds = (int) ((millis % 60000) / 1000);
        int milliseconds = (int) (millis % 1000) / 10;

        return String.format("%02d:%02d,%02d", minutes, seconds, milliseconds);
    }

}