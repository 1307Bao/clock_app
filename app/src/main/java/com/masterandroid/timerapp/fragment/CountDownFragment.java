package com.masterandroid.timerapp.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.masterandroid.timerapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountDownFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountDownFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnChange, btnStart, btnReset;
    private Chronometer chronometer;
    private long countDownTime;
    private long timeLeft = 0;
    private ProgressBar progressBar;
    private boolean isPressed = false;

    public CountDownFragment() {
        // Required empty public constructor
        countDownTime = 60000;
    }

    public CountDownFragment(long countDownTime){
        this.countDownTime = countDownTime;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CountDownFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CountDownFragment newInstance(String param1, String param2) {
        CountDownFragment fragment = new CountDownFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_count_down, container, false);

        btnChange = view.findViewById(R.id.btnCountdownChange);
        btnReset = view.findViewById(R.id.btnCountDownReset);
        btnStart = view.findViewById(R.id.btnCountdownStart);
        chronometer = view.findViewById(R.id.chronometerCountdown);
        progressBar = view.findViewById(R.id.progressBar);

        progressBar.setMax((int) countDownTime);
        progressBar.setProgress((int) countDownTime);
        updateBtnStart("#26bc4b", "#092911");
        formarttedTime(countDownTime);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                timeLeft = chronometer.getBase() - SystemClock.elapsedRealtime();
                if (timeLeft <= 0){
                    chronometer.stop();
                    chronometer.setText("00:00");
                    Toast.makeText(getActivity(), "Hết giờ!", Toast.LENGTH_SHORT).show();
                    progressBar.setProgress(0);
                } else{
                    formarttedTime(timeLeft);
                    progressBar.setProgress((int) timeLeft);
                }
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CountdownChangeFragment());
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPressed = !isPressed;
                if (isPressed){
                    updateBtnStart("#eb4d48", "#340e0b");

                    btnStart.setText("Dừng");
                    chronometer.setBase(SystemClock.elapsedRealtime() + countDownTime - timeLeft);
                    chronometer.start();
                } else {
                    updateBtnStart("#26bc4b", "#092911");
                    btnStart.setText("Bắt đầu");
                    timeLeft = countDownTime - chronometer.getBase() + SystemClock.elapsedRealtime();
                    chronometer.stop();
                    progressBar.setProgress((int) countDownTime - (int) timeLeft);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                timeLeft = 0;
                btnStart.setText("Bắt đầu");
                updateBtnStart("#26bc4b", "#092911");
                isPressed = false;
                progressBar.setProgress((int) countDownTime);
                formarttedTime(countDownTime);
            }
        });

        return view;
    }

    private void replaceFragment (Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();


    }

    private void formarttedTime(long time){
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        chronometer.setText(timeFormatted);
    }

    private void updateBtnStart(String colorText, String colorBackground) {
        btnStart.setBackgroundColor(Color.parseColor(colorBackground));
        btnStart.setTextColor(Color.parseColor(colorText));
    }
}