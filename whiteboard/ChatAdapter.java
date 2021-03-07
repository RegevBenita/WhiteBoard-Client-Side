package com.whiteboard.regi.whiteboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whiteboard.regi.model.Message;

import java.util.List;

//custom adapter that use for implement chat listview
public class ChatAdapter extends ArrayAdapter<Message> {
    private Context context;

    public ChatAdapter(Context context, int resource, List<Message> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder=null;
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chat_raw, null);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        Message message = getItem(position);
        if(message!=null) {
            viewHolder.tvMessage.setText(message.getText());
            viewHolder.tvName.setText(message.getUserName());
            viewHolder.tvName.setTextColor(message.getUserColor());
        }
        return row;
    }

    //class that represent a row
    public class ViewHolder {
        TextView tvName;
        TextView tvMessage;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tvChatName);
            tvMessage = (TextView) view.findViewById(R.id.tvChatMessage);
        }
    }
}
