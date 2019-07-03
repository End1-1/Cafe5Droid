package com.example.cafe5droid.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cafe5droid.Adapters.OrderAdapter;
import com.example.cafe5droid.Classes.CActivity;
import com.example.cafe5droid.Classes.CMenu;
import com.example.cafe5droid.Classes.CPref;
import com.example.cafe5droid.Fragments.Card;
import com.example.cafe5droid.Fragments.Order;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SDish;
import com.example.cafe5droid.Structures.STable;

public class AOrder extends CActivity implements  OrderAdapter.OrderDishSelected {

    public final String FRAGMENT_ORDER = "FRAGMENT_ORDER";
    public boolean allowBack = false;
    private Order order;
    private Card card;
    private TextView place;
    private ImageView ivCheckout;
    public OrderAdapter adOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        place = textView(R.id.tvTable);
        ivCheckout = imageView(R.id.ivShowCard, this);
        STable table = (STable) getIntent().getSerializableExtra("table");
        if (table != null) {
            place.setText(String.format("%s: %s", getString(R.string.Place), table.name));
        }
        if (CMenu.listOfDishes.isEmpty()) {
            createProgressDialog(R.string.Empty, R.string.ProcessMenu);
            CMenu.loadFromDB(this);
            hideProgressDialog();
        }
        if (CPref.menuName.isEmpty() == false) {
            //Filter menu
        }
        adOrder = new OrderAdapter(getLayoutInflater(), this);
        order = new Order();
        card = new Card();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.lvContainer, order, FRAGMENT_ORDER);
        fragmentTransaction.commit();
    }

    @Override
    protected void buttonClick(int id) {
        super.buttonClick(id);
        switch (id) {
            case R.id.ivShowCard:
                adOrder.orderDishSelected = card;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.lvContainer, card, FRAGMENT_ORDER);
                fragmentTransaction.commit();
                break;
        }
    }

    public void goToMenu() {
        adOrder.orderDishSelected = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lvContainer, order, FRAGMENT_ORDER);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (CPref.settingsPassword.isEmpty() || allowBack) {
            CPref.setLastTable(this, 0);
            super.onBackPressed();
        }
    }

    @Override
    public void orderDishSelected(SDish dish) {

    }
}
