package com.masterandroid.timerapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.masterandroid.timerapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountdownChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountdownChangeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnChange, btnCancel;
    private NumberPicker numberPickerMinute, numberPickerSecond;

    public CountdownChangeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CountdownChangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CountdownChangeFragment newInstance(String param1, String param2) {
        CountdownChangeFragment fragment = new CountdownChangeFragment();
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
        View view = inflater.inflate(R.layout.fragment_countdown_change, container, false);

        btnCancel = view.findViewById(R.id.cancelAddCountdownBtn);
        btnChange = view.findViewById(R.id.changeCountdownBtn);

        numberPickerMinute = view.findViewById(R.id.numberPickerMinuteCountdown);
        numberPickerSecond = view.findViewById(R.id.numberPickerSecondCountdown);

        numberPickerMinute.setMaxValue(59);
        numberPickerSecond.setMaxValue(59);
        numberPickerMinute.setMinValue(0);
        numberPickerSecond.setMinValue(0);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CountDownFragment());
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long milisecond = numberPickerSecond.getValue() * 1000 + numberPickerMinute.getValue() * 1000 * 60;
                replaceFragment(new CountDownFragment(milisecond));
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
}