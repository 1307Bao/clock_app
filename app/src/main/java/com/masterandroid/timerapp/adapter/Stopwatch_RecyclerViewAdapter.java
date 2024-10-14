package com.masterandroid.timerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masterandroid.timerapp.R;
import com.masterandroid.timerapp.model.StopwatchModel;

import java.util.ArrayList;

public class Stopwatch_RecyclerViewAdapter extends RecyclerView.Adapter<Stopwatch_RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<StopwatchModel> stopwatchModels;

    public Stopwatch_RecyclerViewAdapter(Context context, ArrayList<StopwatchModel> stopwatchModels) {
        this.context = context;
        this.stopwatchModels = stopwatchModels;
    }

    @NonNull
    @Override
    public Stopwatch_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stopwatch_items, parent, false);

        return new Stopwatch_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Stopwatch_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvId.setText(stopwatchModels.get(position).getId() + "");
        holder.tvTime.setText(stopwatchModels.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return stopwatchModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvTime;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            tvId = itemView.findViewById(R.id.textViewCount);
            tvTime = itemView.findViewById(R.id.textViewTime);
        }
    }

    public void addItem(StopwatchModel stopwatchModel) {
        stopwatchModels.add(stopwatchModel);
        notifyItemInserted(stopwatchModels.size() - 1);
    }

}
