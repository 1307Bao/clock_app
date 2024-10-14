package com.masterandroid.timerapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.masterandroid.timerapp.R;
import com.masterandroid.timerapp.adapter.TimeAlarm_RecyclerViewAdapter;
import com.masterandroid.timerapp.database.DatabaseHandler;
import com.masterandroid.timerapp.event.RecyclerViewInterface;
import com.masterandroid.timerapp.model.TimeAlarmModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Declare Variables
    private ArrayList<TimeAlarmModel> timeAlarmModelArrayList;
    private TimeAlarm_RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseHandler dbHandler;
    private Button btnAlarmAdd;


    public AlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
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
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        if (getContext()!=null) {
            initData();

            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerViewAdapter = new TimeAlarm_RecyclerViewAdapter(getContext(),
                    timeAlarmModelArrayList, this);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        } else {

            Log.e("AlarmFragment", "Fragment chưa được đính kèm vào Activity");
        }

        btnAlarmAdd = view.findViewById(R.id.btnAlarmAdd);
        btnAlarmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new AlarmAddedFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    private void initData() {
        if (getContext() != null) {
            dbHandler = new DatabaseHandler(getContext());
            timeAlarmModelArrayList = dbHandler.getAllRow();
            if (timeAlarmModelArrayList == null) {
                timeAlarmModelArrayList = new ArrayList<>();
            }
        }
    }

    @Override
    public void onItemLongClick(int position) {
        int id = timeAlarmModelArrayList.get(position).getId();
        dbHandler.deleteRow(id);
        timeAlarmModelArrayList.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
    }
}