package com.example.cafe5droid.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SDish;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static ArrayList<SDish> dishes = new ArrayList<>();

    private LayoutInflater layoutInflater;
    public OrderDishSelected orderDishSelected;

    public interface OrderDishSelected {
        void orderDishSelected(SDish dish);
    }

    private final class OrderAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivPlus;
        private ImageView ivMinus;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvTotal;
        private EditText edQty;
        private SDish dish;

        public OrderAdapterViewHolder(@NonNull View v) {
            super(v);
            ivPlus = v.findViewById(R.id.ivPlus);
            ivPlus.setOnClickListener(this);
            ivMinus = v.findViewById(R.id.ivMinus);
            ivMinus.setOnClickListener(this);
            edQty = v.findViewById(R.id.edQty);
            edQty.setText("1");
            tvName = v.findViewById(R.id.tvName);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvTotal = v.findViewById(R.id.tvTotal);
        }

        @Override
        public void onClick(View v) {
            Integer qty = 0;
            switch (v.getId()) {
                case R.id.ivPlus:
                    qty = Integer.valueOf(edQty.getText().toString());
                    qty++;
                    dish.qty1 = qty.toString();
                    edQty.setText(qty.toString());
                    tvTotal.setText(String.format("%d", qty * Integer.valueOf(tvPrice.getText().toString())));
                    if (orderDishSelected != null) {
                        orderDishSelected.orderDishSelected(dish);
                    }
                    break;
                case R.id.ivMinus:
                    qty = Integer.valueOf(edQty.getText().toString());
                    if (qty > 1) {
                        qty--;
                    }
                    edQty.setText(qty.toString());
                    dish.qty1 = qty.toString();
                    tvTotal.setText(String.format("%d", qty * Integer.valueOf(tvPrice.getText().toString())));
                    if (orderDishSelected != null) {
                        orderDishSelected.orderDishSelected(dish);
                    }
                    break;
            }
        }

        public void bind(int i) {
            dish = dishes.get(i);
            tvName.setText(dish.name);
            tvPrice.setText(dish.price);
            edQty.setText(dish.qty1);
            Integer qty = Integer.valueOf(edQty.getText().toString());
            tvTotal.setText(String.format("%d", qty * Integer.valueOf(dish.price)));
        }
    }

    public void addDish(SDish d) {
        SDish dish = d;
        dishes.add(dish);
        notifyDataSetChanged();
        if (orderDishSelected != null) {
            orderDishSelected.orderDishSelected(dish);
        }
    }

    public String total() {
        Integer total = 0;
        for (int i = 0; i < dishes.size(); i++) {
            String price = dishes.get(i).price;
            if (price == null) {
                price = "0";
            }
            total += Integer.valueOf(price) * Integer.valueOf(dishes.get(i).qty1);
        }
        return total.toString();
    }

    public OrderAdapter(LayoutInflater inflater, OrderDishSelected listener) {
        layoutInflater = inflater;
        orderDishSelected = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderAdapterViewHolder(layoutInflater.inflate(R.layout.item_orderdish, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        OrderAdapterViewHolder holder = (OrderAdapterViewHolder) viewHolder;
        holder.bind(i);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }
}
