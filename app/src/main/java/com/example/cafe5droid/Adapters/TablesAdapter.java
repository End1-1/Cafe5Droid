package com.example.cafe5droid.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cafe5droid.Classes.CHall;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.STable;

public class TablesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private TablesOptionSelected tablesOptionSelected;

    public final class TablesAdaptegerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;

        public TablesAdaptegerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = itemView.findViewById(R.id.idTableName);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            tablesOptionSelected.tablesOptionSelected(CHall.table(i));
        }

        void bind(STable table) {
            tvName.setText(table.name);
        }
    }

    public interface TablesOptionSelected {
        void tablesOptionSelected(STable table);
    }

    public TablesAdapter(Context context, TablesOptionSelected listener) {
        layoutInflater = LayoutInflater.from(context);
        tablesOptionSelected = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TablesAdaptegerViewHolder(layoutInflater.inflate(R.layout.item_table, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TablesAdaptegerViewHolder holder = (TablesAdaptegerViewHolder) viewHolder;
        holder.bind(CHall.table(i));
    }

    @Override
    public int getItemCount() {
        return CHall.listOfFilteredTables.size();
    }
}
