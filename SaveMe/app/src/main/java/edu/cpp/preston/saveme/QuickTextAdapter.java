package edu.cpp.preston.saveme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

//Adapter for quicktext list view
public class QuickTextAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> messages;

    public QuickTextAdapter(Context context, ArrayList<String> messages) {
        super(context, R.layout.item_notification, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_quick_text, parent, false);

        TextView messagePreviewText = (TextView) view.findViewById(R.id.quickTextPreviewText);
        String message = messages.get(position);

        if (message.length() > 33){
            message = message.substring(0, 30) + "...";
        }

        messagePreviewText.setText(message);

        return view;
    }
}