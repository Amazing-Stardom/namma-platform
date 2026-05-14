package com.nammaplatform.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * RecyclerView Adapter for displaying train cards in TrainListActivity.
 */
public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {

    private final Context context;
    private final ArrayList<Train> trainList;

    public TrainAdapter(Context context, ArrayList<Train> trainList) {
        this.context = context;
        this.trainList = trainList;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_train, parent, false);
        return new TrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
        Train train = trainList.get(position);

        // Set train name (large bold blue text)
        holder.tvTrainName.setText(train.getTrainName());

        // Set departure time with clock emoji
        holder.tvDeparture.setText(context.getString(R.string.departure_prefix) + train.getDeparture());

        // Set platform badge
        holder.tvPlatform.setText(context.getString(R.string.platform_label) + train.getPlatform());

        // Handle "View Coach Layout" button click
        holder.btnViewCoach.setOnClickListener(v -> {
            Intent intent = new Intent(context, CoachLayoutActivity.class);
            intent.putExtra("trainName", train.getTrainName());
            intent.putExtra("departure", train.getDeparture());
            intent.putExtra("platform", train.getPlatform());
            intent.putStringArrayListExtra("coaches", train.getCoaches());
            context.startActivity(intent);
        });

        // Also allow clicking the entire card to navigate
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CoachLayoutActivity.class);
            intent.putExtra("trainName", train.getTrainName());
            intent.putExtra("departure", train.getDeparture());
            intent.putExtra("platform", train.getPlatform());
            intent.putStringArrayListExtra("coaches", train.getCoaches());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trainList != null ? trainList.size() : 0;
    }

    /**
     * ViewHolder holding references to each card view's child widgets.
     */
    static class TrainViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainName;
        TextView tvDeparture;
        TextView tvPlatform;
        Button btnViewCoach;

        TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainName = itemView.findViewById(R.id.tv_train_name);
            tvDeparture = itemView.findViewById(R.id.tv_departure);
            tvPlatform = itemView.findViewById(R.id.tv_platform_badge);
            btnViewCoach = itemView.findViewById(R.id.btn_view_coach);
        }
    }
}
