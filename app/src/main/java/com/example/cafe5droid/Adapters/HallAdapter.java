package com.example.cafe5droid.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cafe5droid.Classes.CHall;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SHall;

public class HallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    private HallAdapterSelected hallAdapterSelected;

    public final class HallAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;

        public HallAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = itemView.findViewById(R.id.idName);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            hallAdapterSelected.hallOptionSelected(CHall.hall(i));
        }

        void bind(SHall hall) {
            tvName.setText(hall.name);
        }
    }

    public interface HallAdapterSelected {
        void hallOptionSelected(SHall hall);
    }

    public HallAdapter(Context context, HallAdapterSelected listener) {
        layoutInflater = LayoutInflater.from(context);
        hallAdapterSelected = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HallAdapterViewHolder(layoutInflater.inflate(R.layout.item_hall, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        HallAdapterViewHolder holder = (HallAdapterViewHolder)viewHolder;
        holder.bind(CHall.hall(i));
    }

    @Override
    public int getItemCount() {
        return CHall.listOfHall.size();
    }
}
