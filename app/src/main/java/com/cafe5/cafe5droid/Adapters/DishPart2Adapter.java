package com.cafe5.cafe5droid.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafe5.cafe5droid.Classes.CMenu;
import com.cafe5.cafe5droid.R;

public class DishPart2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    private DishPart2AdapterSelected dishPart1AdapterSelected;

    public interface DishPart2AdapterSelected {
        void dishPart2Selected(String name);
    }

    private final class DishPart2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private ImageView ivDishType;

        public DishPart2ViewHolder(@NonNull View v) {
            super(v);
            v.findViewById(R.id.lvDishPart2).setOnClickListener(this);
            tvName = v.findViewById(R.id.tvName);
            ivDishType = v.findViewById(R.id.ivDishType);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            dishPart1AdapterSelected.dishPart2Selected(CMenu.filterPart2.get(i));
        }

        public void bind(String name) {
            tvName.setText(name);
        }
    }

    public DishPart2Adapter(LayoutInflater inflater, DishPart2AdapterSelected listener) {
        layoutInflater = inflater;
        dishPart1AdapterSelected = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DishPart2ViewHolder(layoutInflater.inflate(R.layout.item_dishpart2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DishPart2ViewHolder holder = (DishPart2ViewHolder) viewHolder;
        holder.bind(CMenu.filterPart2.get(i));
    }

    @Override
    public int getItemCount() {
        return CMenu.filterPart2.size();
    }
}
