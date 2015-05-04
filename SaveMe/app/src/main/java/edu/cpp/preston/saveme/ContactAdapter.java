package edu.cpp.preston.saveme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//Adapter for contacts list view
public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    protected ArrayList<Contact> contacts;
    private boolean showCheckBox;

    public ContactAdapter(Context context, ArrayList<Contact> contacts, boolean showCheckBox) {
        super(context, R.layout.item_contact, contacts);
        this.context = context;
        this.contacts = contacts;
        this.showCheckBox = showCheckBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Contact contact = contacts.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.contactTypeIcon);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView contactIDText = (TextView) view.findViewById(R.id.contactIDText);
        final CheckBox checkbox = (CheckBox) view.findViewById(R.id.contactCheckBox);

        nameText.setText(contact.getDisplayName());
        contactIDText.setText(contact.getUsernameOrNumber());

        if (!checkbox.isChecked() && contact.isSelected()){
            checkbox.setChecked(true);
        } else if (checkbox.isChecked() && !contact.isSelected()){
            checkbox.setChecked(false);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showCheckBox) { //in contacts activity

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(contacts.get(position).getDisplayName() + "\n" + contacts.get(position).getUsernameOrNumber());

                    builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    //delete number
                    builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences sharedPrefContacts = context.getSharedPreferences(context.getString(R.string.preference_file_contacts_key), Context.MODE_PRIVATE);

                            for (int j = 0; j < 50; j++){ //removes contact from preferences
                                if (sharedPrefContacts.getString("usernameOrNumber" + j, "*").equalsIgnoreCase(contacts.get(position).getUsernameOrNumber())){
                                    SharedPreferences.Editor editor = sharedPrefContacts.edit();
                                    editor.remove("displayname" + j);
                                    editor.remove("usernameOrNumber" + j);
                                    editor.remove("isNumber" + j);
                                    editor.remove("isConfirmed" + j);
                                    editor.remove("userObjectId" + j);
                                    editor.commit();
                                    break;
                                }
                            }

                            //TODO if contact is not confirmed delete notification on server here?

                            contacts.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    builder.create().show();
                } else { // in customize alert
                    if (checkbox.isChecked()) {
                        checkbox.setChecked(false);
                        contact.setSelected(false);
                    } else {
                        checkbox.setChecked(true);
                        contact.setSelected(true);
                    }
                }
            }
        });

        if (!showCheckBox){
            checkbox.setVisibility(View.GONE);
        }

        if (contact.getClass() == ContactPhone.class){ //contact is a phone number
            imageView.setImageResource(R.drawable.contactphone);
        } else { //contact is a Save Me user name
            ContactSaveMe contactCasted = (ContactSaveMe)contact;

            if (!showCheckBox){ //means your in contacts activity
                if (contactCasted.isConfirmed()){
                    imageView.setImageResource(R.drawable.contactuserconfirmed);
                } else{
                    contactIDText.setText(contactCasted.getUsernameOrNumber() + " - Requires confirmation before use.");
                    imageView.setImageResource(R.drawable.contactusernotconfirmed);
                }
            } else { //means your in customize alert activity
                imageView.setImageResource(R.drawable.contactuser);
            }
        }

        return view;
    }

}