package com.example.cafe5droid.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cafe5droid.Classes.CMenu;
import com.example.cafe5droid.R;

public class DishPart1Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    private DishPart1AdapterSelected dishPart1AdapterSelected;

    public interface DishPart1AdapterSelected {
        void dishPart1Selected(String name);
    }

    private final class DishPart1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;

        public DishPart1ViewHolder(@NonNull View v) {
            super(v);
            v.setOnClickListener(this);
            tvName = v.findViewById(R.id.tvName);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            dishPart1AdapterSelected.dishPart1Selected(CMenu.listOfPart1.get(i));
        }

        public void bind(String name) {
            tvName.setText(name);
        }
    }

    public DishPart1Adapter(LayoutInflater inflater, DishPart1AdapterSelected listener) {
        layoutInflater = inflater;
        dishPart1AdapterSelected = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DishPart1ViewHolder(layoutInflater.inflate(R.layout.item_dishpart1, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DishPart1ViewHolder holder = (DishPart1ViewHolder) viewHolder;
        holder.bind(CMenu.listOfPart1.get(i));
    }

    @Override
    public int getItemCount() {
        return CMenu.listOfPart1.size();
    }
}
