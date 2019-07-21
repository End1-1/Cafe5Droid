package com.cafe5.cafe5droid.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafe5.cafe5droid.Activities.AOrder;
import com.cafe5.cafe5droid.Adapters.OrderAdapter;
import com.cafe5.cafe5droid.R;
import com.cafe5.cafe5droid.Structures.SDish;

public class Card extends Fragment implements View.OnClickListener, OrderAdapter.OrderDishSelected{

    private ImageView ivBack;
    private TextView tvTotal;
    private TextView tvCheckout;
    private RecyclerView rvOrder;
    private OrderAdapter adOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        ivBack = v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        tvCheckout = v.findViewById(R.id.tvCheckout);
        tvCheckout.setOnClickListener(this);
        tvTotal = v.findViewById(R.id.tvAmount);
        rvOrder = v.findViewById(R.id.rvCard);
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        adOrder = ((AOrder) getActivity()).adOrder;
        adOrder.orderDishSelected = this;
        rvOrder.setAdapter(adOrder);
        tvTotal.setText(String.format("%s - %s", getString(R.string.TotalAmount), adOrder.total()));
        return v;
    }

    @Override
    public void onClick(View v) {
        AOrder o = (AOrder) getActivity();
        switch (v.getId()) {
            case R.id.ivBack:
                o.goToMenu();
                break;
            case R.id.tvCheckout:
                o.checkout();
                break;
        }
    }

    @Override
    public void orderDishSelected(SDish dish) {
        tvTotal.setText(String.format("%s - %s", getString(R.string.TotalAmount), adOrder.total()));
    }
}
