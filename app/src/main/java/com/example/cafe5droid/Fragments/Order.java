package com.example.cafe5droid.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafe5droid.Activities.AOrder;
import com.example.cafe5droid.Adapters.DishAdapter;
import com.example.cafe5droid.Adapters.DishPart1Adapter;
import com.example.cafe5droid.Adapters.DishPart2Adapter;
import com.example.cafe5droid.Adapters.OrderAdapter;
import com.example.cafe5droid.Classes.CDatabase;
import com.example.cafe5droid.Classes.CMenu;
import com.example.cafe5droid.Classes.CPref;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SDish;

public class Order extends Fragment implements DishPart1Adapter.DishPart1AdapterSelected, DishPart2Adapter.DishPart2AdapterSelected,
        DishAdapter.DishAdapterSelected, View.OnClickListener {

    private RecyclerView rvDishPart1;
    private RecyclerView rvDishPart2;
    private RecyclerView rvDish;
    private ImageView ivExpandPart2;
    private ImageView ivDept;
    private TextView tvDept;
    private TextView tvPart2;
    private String menu;
    private String part1;
    private String part2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        DishPart1Adapter adDishPart1 = new DishPart1Adapter(inflater, this);
        rvDishPart1 = v.findViewById(R.id.rvDept);
        rvDishPart1.setLayoutManager(new GridLayoutManager(getActivity(), CMenu.listOfPart1.size() == 0 ? 1 : CMenu.listOfPart1.size()));
        rvDishPart1.setAdapter(adDishPart1);
        tvDept = v.findViewById(R.id.tvDept);
        tvDept.setOnClickListener(this);
        tvPart2 = v.findViewById(R.id.tvDishTypeName);
        tvPart2.setOnClickListener(this);
        DishPart2Adapter adDishPart2 = new DishPart2Adapter(inflater, this);
        rvDishPart2 = v.findViewById(R.id.rvPart2);
        int spanCount = 1;
        rvDishPart2.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        rvDishPart2.setAdapter(adDishPart2);
        ivExpandPart2 = v.findViewById(R.id.ivExpandDishType);
        ivExpandPart2.setOnClickListener(this);
        ivDept = v.findViewById(R.id.ivDept);
        ivDept.setOnClickListener(this);
        DishAdapter adDish = new DishAdapter(inflater, this);
        rvDish = v.findViewById(R.id.rvDishes);
        rvDish.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rvDish.setAdapter(adDish);

        if (CPref.menuName.isEmpty() == false) {
            menu = CPref.menuName;
            filterMenu();
        }
        return v;
    }

    @Override
    public void dishPart1Selected(String name) {
        rvDishPart1.setVisibility(View.GONE);
        tvDept.setText(name);
        tvDept.setVisibility(View.VISIBLE);
        part1 = name;
        part2 = null;
        tvPart2.setText(getText(R.string.DishType));
        filterMenu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDept:
                if (CMenu.listOfPart1.isEmpty()) {
                    return;
                }
                rvDishPart1.setVisibility(View.VISIBLE);
                tvDept.setVisibility(View.GONE);
                break;
            case R.id.ivExpandDishType:
                if (rvDishPart2.getVisibility() == View.GONE) {
                    ivExpandPart2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.iv_expander_off));
                    rvDishPart2.setVisibility(View.VISIBLE);
                    rvDish.setVisibility(View.GONE);
                } else {
                    ivExpandPart2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.iv_expander_on));
                    rvDishPart2.setVisibility(View.GONE);
                    rvDish.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tvDishTypeName:
                onClick(ivExpandPart2);
                break;
            case R.id.ivDept:
                onClick(tvDept);
                break;
        }
    }

    @Override
    public void dishPart2Selected(String name) {
        onClick(ivExpandPart2);
        tvPart2.setText(name);
        part2 = name;
        filterMenu();
    }

    private void filterMenu() {
        CMenu.filter(menu, part1, part2);
        rvDishPart2.getAdapter().notifyDataSetChanged();
        rvDish.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void dishSelected(SDish dish) {
        AOrder ao = (AOrder) getActivity();
        ao.addDish(dish);
    }
}
