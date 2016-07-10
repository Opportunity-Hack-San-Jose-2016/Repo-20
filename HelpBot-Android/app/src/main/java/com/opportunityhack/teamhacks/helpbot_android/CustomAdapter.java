package com.opportunityhack.teamhacks.helpbot_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hjagtap on 7/9/16.
 */
public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<RowItem> rowItems;

    public CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public RowItem getItem(int i) {
        return rowItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rowItems.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.row_list_item, null);
        }

        TextView refugeeID = (TextView) view.findViewById(R.id.refugee_list_id);
        TextView refugeeZip = (TextView) view.findViewById(R.id.refugee_list_zip);

        RowItem row = rowItems.get(i);
        refugeeID.setText(row.getRefugeeID());
        refugeeZip.setText(row.getZip());

        return view;
    }
}
