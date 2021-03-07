package com.whiteboard.regi.whiteboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whiteboard.regi.model.Group;

import java.util.List;

public class GroupAdapter extends ArrayAdapter<Group> {
    private Context context;

    public GroupAdapter(Context context, int resource, List<Group> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        Group group = getItem(position);
        textView.setText(group.getName());
        return row;
    }
}
