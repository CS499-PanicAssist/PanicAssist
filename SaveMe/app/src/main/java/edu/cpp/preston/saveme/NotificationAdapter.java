package edu.cpp.preston.saveme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

//Adapter for notifications list view
public class NotificationAdapter extends ArrayAdapter<Notification> {

    private Context context;
    private ArrayList<Notification> notifications;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, R.layout.item_notification, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_notification, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.notificationIcon);
        TextView titleText = (TextView) view.findViewById(R.id.notificationNameText);
        TextView detailText = (TextView) view.findViewById(R.id.notificationDetailText);

        if (notifications.get(position).getClass() == NotificationInfo.class){ //info message
            imageView.setImageResource(android.R.drawable.ic_dialog_info);
        } else if (notifications.get(position).getClass() == NotificationAlert.class){ //alert
            imageView.setImageResource(android.R.drawable.ic_dialog_alert);
        } else if (notifications.get(position).getClass() == NotificationRequest.class){ //contact request
            imageView.setImageResource(android.R.drawable.ic_dialog_email);
        } else {
            //TODO throw error here
        }

        titleText.setText(notifications.get(position).getTitle());
        detailText.setText(notifications.get(position).getDetailedTitle());
        return view;
    }
}