package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.TableModel;

import java.util.ArrayList;

/**
 * Created by admin on 4/9/2018.
 */

public class TableAdapter extends BaseAdapter {
    Context context;
    ArrayList<TableModel.list> tableList;
    LayoutInflater inflter;
    public TableAdapter(Context context, ArrayList<TableModel.list> tableList) {
        this.context = context;
        this.tableList = tableList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return tableList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TableModel.list item =tableList.get(i);

        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView textView=(TextView)view.findViewById(R.id.text);
        textView.setText(item.getName());
        return view;
    }
}
