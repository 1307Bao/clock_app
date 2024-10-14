package com.masterandroid.timerapp.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masterandroid.timerapp.AlarmReceiver;
import com.masterandroid.timerapp.R;
import com.masterandroid.timerapp.database.DatabaseHandler;
import com.masterandroid.timerapp.event.RecyclerViewInterface;
import com.masterandroid.timerapp.model.TimeAlarmModel;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeAlarm_RecyclerViewAdapter extends RecyclerView.Adapter<TimeAlarm_RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TimeAlarmModel> timeAlarmModels;
    private final RecyclerViewInterface recyclerViewInterface;

    public TimeAlarm_RecyclerViewAdapter(Context context, ArrayList<TimeAlarmModel> timeAlarmModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.timeAlarmModels = timeAlarmModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TimeAlarm_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alarm_items, parent, false);

        return new TimeAlarm_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeAlarm_RecyclerViewAdapter.MyViewHolder holder, int position) {
        if (holder.tvNote == null || holder.tvMinute == null || holder.tvNote == null){
            Log.d("FailBuild", "null");
        }
        Log.d("AdapterDebug", "id: "+ timeAlarmModels.get(position).getId() +  " Hour: " + timeAlarmModels.get(position).getHour() + ", Minute: " + timeAlarmModels.get(position).getMinute() + ", Note: " + timeAlarmModels.get(position).getNote());

        holder.tvHour.setText(timeAlarmModels.get(position).getHour());
        holder.tvMinute.setText(timeAlarmModels.get(position).getMinute());
        holder.tvNote.setText(timeAlarmModels.get(position).getNote());
        if (timeAlarmModels.get(position).getCheck() == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", timeAlarmModels.get(position).getId());
                contentValues.put("hour", timeAlarmModels.get(position).getHour());
                contentValues.put("minute", timeAlarmModels.get(position).getMinute());
                contentValues.put("note", timeAlarmModels.get(position).getNote());
                if (holder.checkBox.isChecked()){
                    contentValues.put("ischeck", 1);
                    setAlarm(context, timeAlarmModels.get(position));
                } else {
                    contentValues.put("ischeck", 0);
                    cancelAlarm(context, timeAlarmModels.get(position));
                }
                DatabaseHandler databaseHandler = new DatabaseHandler(context);
                databaseHandler.updateRow(timeAlarmModels.get(position).getId(), contentValues);
            }
        });
    }

    @Override
    public int getItemCount() {

        return timeAlarmModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvHour, tvMinute, tvNote;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHour = itemView.findViewById(R.id.textViewHour);
            tvMinute = itemView.findViewById(R.id.textViewMinute);
            tvNote = itemView.findViewById(R.id.textViewNote);
            checkBox = itemView.findViewById(R.id.alarmCheck);

            itemView.setOnLongClickListener((View.OnLongClickListener) view -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemLongClick(position);
                    }
                }
                return true;
            });

        }
    }

    // Set Alarm

    private void setAlarm(Context context, TimeAlarmModel model) {
        // Khởi tạo AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, model.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(model.getHour()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(model.getMinute()));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (alarmManager != null)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void cancelAlarm(Context context, TimeAlarmModel model) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, model.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
