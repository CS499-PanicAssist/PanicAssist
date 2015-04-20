package edu.cpp.preston.saveme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

//Adapter for contacts list view
public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private ArrayList<Contact> contacts;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, R.layout.item_contact, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_contact, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.contactTypeIcon);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView contactIDText = (TextView) view.findViewById(R.id.contactIDText);

        nameText.setText(contacts.get(position).getdisplayName());
        contactIDText.setText(contacts.get(position).getID());

        if (contacts.get(position).isNumber){ //contact is a phone number
            imageView.setImageResource(R.drawable.contactphone);
        } else { //contact is a Save Me user name
            imageView.setImageResource(R.drawable.contactuser);
        }

        return view;
    }
}