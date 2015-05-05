package edu.cpp.preston.PanicAssist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

//Adapter for phone number list view
public class PhoneNumberAdapter extends ArrayAdapter<PhoneNumber> {

    private Context context;
    private ArrayList<PhoneNumber> phoneNumbers;

    public PhoneNumberAdapter(Context context, ArrayList<PhoneNumber> phoneNumbers) {
        super(context, R.layout.item_phone_number, phoneNumbers);
        this.context = context;
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_phone_number, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.Icon);
        TextView nameText = (TextView) view.findViewById(R.id.NameText);
        TextView numberText = (TextView) view.findViewById(R.id.NumberText);

        imageView.setImageResource(android.R.drawable.ic_menu_call);
        nameText.setText(phoneNumbers.get(position).getName());
        numberText.setText(phoneNumbers.get(position).getNumber());

        return view;
    }
}