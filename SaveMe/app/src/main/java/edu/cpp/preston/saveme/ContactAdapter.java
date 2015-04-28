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
    private boolean showCheckBox;

    public ContactAdapter(Context context, ArrayList<Contact> contacts, boolean showCheckBox) {
        super(context, R.layout.item_contact, contacts);
        this.context = context;
        this.contacts = contacts;
        this.showCheckBox = showCheckBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_contact, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.contactTypeIcon);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView contactIDText = (TextView) view.findViewById(R.id.contactIDText);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.contactCheckBox);

        nameText.setText(contacts.get(position).getdisplayName());
        contactIDText.setText(contacts.get(position).getID());

        if (!showCheckBox){
            checkBox.setVisibility(View.GONE);
        }

        if (contacts.get(position).isNumber){ //contact is a phone number
            imageView.setImageResource(R.drawable.contactphone);
        } else { //contact is a Save Me user name
            if (!showCheckBox){ //means your in contacts activity
                if (contacts.get(position).isConfirmed){
                    imageView.setImageResource(R.drawable.contactuserconfirmed);
                } else{
                    contactIDText.setText(contacts.get(position).getID() + " - Requires confirmation before use.");
                    imageView.setImageResource(R.drawable.contactusernotconfirmed);
                }
            } else { //means your in customize alert activity
                imageView.setImageResource(R.drawable.contactuser);
            }
        }

        return view;
    }
}