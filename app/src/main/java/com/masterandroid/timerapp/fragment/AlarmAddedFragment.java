package com.masterandroid.timerapp.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.masterandroid.timerapp.AlarmReceiver;
import com.masterandroid.timerapp.R;
import com.masterandroid.timerapp.database.DatabaseHandler;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmAddedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmAddedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button btnAdd, btnCancel;
    private NumberPicker numberPickerHour, numberPickerMinute;
    private EditText editTextNote;
    private DatabaseHandler db;

    public AlarmAddedFragment() {
        // Required empty public constructor
    }

    public static AlarmAddedFragment newInstance(String param1, String param2) {
        AlarmAddedFragment fragment = new AlarmAddedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getContext()); // Khởi tạo ở đây
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_added, container, false);

        editTextNote = view.findViewById(R.id.editTextNote);
        btnAdd = view.findViewById(R.id.addBtn);
        btnCancel = view.findViewById(R.id.cancelAddBtn);
        numberPickerHour = view.findViewById(R.id.numberPickerHour);
        numberPickerMinute = view.findViewById(R.id.numberPickerMinute);

        numberPickerHour.setMaxValue(23);
        numberPickerHour.setMinValue(0);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new AlarmFragment());
                fragmentTransaction.addToBackStack(null); // Thêm vào back stack
                fragmentTransaction.commit();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = numberPickerHour.getValue();
                int minute = numberPickerMinute.getValue();
                String note = editTextNote.getText().toString().trim();

                if (note.isEmpty()) {
                    Toast.makeText(getContext(), "Ghi chú không thể để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("hour", hour);
                contentValues.put("minute", minute);
                contentValues.put("note", note);
                contentValues.put("ischeck", 1);

                db.insertRow(contentValues);

                int idInserted = db.getMaxId();
                Log.d("Items", idInserted + " " + hour + " " + minute);

                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), idInserted, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }


                Toast.makeText(getContext(), "Báo thức đã được thêm", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new AlarmFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        db.close(); // Đóng cơ sở dữ liệu nếu cần
        super.onDestroy();
    }
}
