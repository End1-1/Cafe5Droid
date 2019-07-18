package com.example.cafe5droid.Activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafe5droid.Adapters.OrderAdapter;
import com.example.cafe5droid.Classes.CActivity;
import com.example.cafe5droid.Classes.CDatabase;
import com.example.cafe5droid.Classes.CMenu;
import com.example.cafe5droid.Classes.CPref;
import com.example.cafe5droid.Classes.CSocketClient;
import com.example.cafe5droid.Fragments.Card;
import com.example.cafe5droid.Fragments.Order;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SDish;
import com.example.cafe5droid.Structures.STable;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AOrder extends CActivity implements  OrderAdapter.OrderDishSelected {

    public final String FRAGMENT_ORDER = "FRAGMENT_ORDER";
    public boolean allowBack = false;
    private Order order;
    private Card card;
    private TextView place;
    public STable table;
    private ImageView ivCheckout;
    public OrderAdapter adOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        place = textView(R.id.tvTable);
        ivCheckout = imageView(R.id.ivShowCard, this);
        textView(R.id.tvBasket).setOnClickListener(this);
        table = (STable) getIntent().getSerializableExtra("table");
        if (table != null) {
            place.setText(String.format("%s: %s", getString(R.string.Place), table.name));
        } else {
            alertDialogText(R.string.Error, "Invalid table");
            finish();
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
                adOrder.refreshData(this, table.id);
                adOrder.orderDishSelected = card;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.lvContainer, card, FRAGMENT_ORDER);
                fragmentTransaction.commit();
                break;
            case R.id.tvBasket:
                buttonClick(R.id.ivShowCard);
                break;
        }
    }

    public void checkout() {
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClient.cmd_apporder);
            o.put("tableid", table.id);
            o.put("header", table.header);
            JSONArray ja = new JSONArray();
            for (int i = 0; i < adOrder.getItemCount(); i++) {
                SDish d = adOrder.getDish(i);
                JSONObject jd = new JSONObject();
                jd.put("dishcode", d.id);
                jd.put("dishname", d.name);
                jd.put("adgcode", d.adgCode);
                jd.put("qty1", d.qty1);
                jd.put("qty2", d.qty2);
                jd.put("price", d.price);
                jd.put("print1", d.print1);
                jd.put("print2", d.print2);
                jd.put("store", d.store);
                jd.put("internal id", String.valueOf(d.internalId));
                jd.put("remoteid", d.remoteId);
                ja.put(jd);
            }
            o.put("dishes", ja);
            sendMessage(o.toString(), R.string.Checkout);
        } catch (JSONException e) {
            e.printStackTrace();
            alertDialogText(R.string.Error, e.getMessage());
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
        } else {
            createPasswordDialog(getString(R.string.Empty), 0);
        }
    }

    public void addDish(SDish dish) {
        CDatabase db = new CDatabase(this);
        ContentValues cv = db.getContentValues();
        cv.put("tableid", table.id);
        cv.put("header", table.header);
        cv.put("dishcode", dish.id);
        cv.put("dishname", dish.name);
        cv.put("qty1", "1");
        cv.put("qty2", "0");
        cv.put("price", dish.price);
        cv.put("service", "0");
        cv.put("discount", "0");
        cv.put("total", dish.price);
        cv.put("store", dish.store);
        cv.put("print1", dish.print1);
        cv.put("print2", dish.print2);
        cv.put("comment", "");
        cv.put("remind", "0");
        cv.put("adgcode", dish.adgCode);
        cv.put("removereason", "");
        dish.internalId = db.insertWithId("body");
        if (dish.internalId == 0) {
            alertDialogText(R.string.Error, "Cannot insert into body");
            return;
        }
        adOrder.addDish(dish);
        Toast.makeText(this, String.format("%s + 1", dish.name), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void orderDishSelected(SDish dish) {

    }

    @Override
    public void onPassword(String pwd, int id) {
        if (pwd.equals(CPref.settingsPassword)) {
            finish();
        } else {
            Toast.makeText(this, R.string.AccessDenied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void messageResponse(JSONObject o) {
        super.messageResponse(o);
        try {
            int code = o.getInt("reply");
            switch (code) {
                case CSocketClient.cmd_apporder:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
