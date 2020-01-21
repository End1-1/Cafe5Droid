package com.cafe5.cafe5droid.Adapters;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafe5.cafe5droid.Classes.CMenu;
import com.cafe5.cafe5droid.R;
import com.cafe5.cafe5droid.Structures.SDish;

public class DishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private DishAdapterSelected dishAdapterSelected;

    public interface DishAdapterSelected {
        void dishSelected(SDish dish);
    }

    private final class DishAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SDish dish;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvDescription;
        private ImageView imageView;
        private ImageView imgCart;

        public DishAdapterViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvDescription = v.findViewById(R.id.tvDescription);
            imageView = v.findViewById(R.id.ivDish);
            imgCart = v.findViewById(R.id.ivAddToCard);
            imgCart.setOnClickListener(this);
            imgCart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.getBackground().setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                    }
                    return false;
                }
            });
        }

        public void bind(SDish d) {
            dish = d;
            tvName.setText(dish.name);
            tvPrice.setText(dish.price);
            tvDescription.setText(dish.description);
            if (dish.image == null || dish.image.isEmpty()) {
                imageView.setImageDrawable(layoutInflater.getContext().getResources().getDrawable(R.drawable.dishtype));
            } else {
                //load from resource
            }
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            dishAdapterSelected.dishSelected(CMenu.filterDish.get(i));
        }
    }

    public DishAdapter(LayoutInflater inflater, DishAdapterSelected listener) {
        layoutInflater = inflater;
        dishAdapterSelected = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DishAdapterViewHolder(layoutInflater.inflate(R.layout.item_dish, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DishAdapterViewHolder holder = (DishAdapterViewHolder) viewHolder;
        holder.bind(CMenu.filterDish.get(i));
    }

    @Override
    public int getItemCount() {
        return CMenu.filterDish.size();
    }
}
