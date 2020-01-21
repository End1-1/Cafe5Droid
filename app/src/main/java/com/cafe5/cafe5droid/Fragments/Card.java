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
import com.cafe5.cafe5droid.Classes.CPref;
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
        v.findViewById(R.id.tvReceipt).setOnClickListener(this);
        tvTotal = v.findViewById(R.id.tvAmount);
        rvOrder = v.findViewById(R.id.rvCard);
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        adOrder = ((AOrder) getActivity()).adOrder;
        adOrder.orderDishSelected = this;
        rvOrder.setAdapter(adOrder);
        orderDishSelected(null);
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
            case R.id.tvReceipt:
                o.callReceipt();
                break;
        }
    }

    @Override
    public void orderDishSelected(SDish dish) {
        String fin = getString(R.string.Counted) + ": " + Float.valueOf(adOrder.total()).toString() ;
        if (CPref.serviceValue > 0.001) {
            fin  += "\r\n" + getString(R.string.ServiceAmount) + "(" + Float.toString(CPref.serviceValue * 100) + "%): " + Float.toString(Float.valueOf(adOrder.total()) * CPref.serviceValue);
            fin += "\r\n" + getString(R.string.TotalAmount) +": " + Float.toString(Float.valueOf(adOrder.total()) + (Float.valueOf(adOrder.total()) * CPref.serviceValue));
        }
        tvTotal.setText(fin);
    }
}

