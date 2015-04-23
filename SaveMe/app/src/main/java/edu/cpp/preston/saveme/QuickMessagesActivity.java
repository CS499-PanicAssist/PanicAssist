package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;

public class QuickMessagesActivity extends ActionBarActivity {

    private ArrayList<String> quickTexts;
    private QuickTextAdapter quickTextListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_messages);


        //sets up notification area
        quickTexts = new ArrayList<String>(); //TODO populate quicktexts from file
        quickTexts.add("Help I'm in trouble");
        quickTexts.add("Please call me. If I don't pick up I may need help");
        quickTexts.add("This is just letting you know my location, thanks");


        ListView listView = (ListView) findViewById(R.id.quickTextListView);
        quickTextListAdapter = new QuickTextAdapter(this, quickTexts);
        listView.setAdapter(quickTextListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQuickTextDialog(quickTexts.get(i), i); //shows dialog for notification clicked
            }
        });

        ImageButton addImageButton = (ImageButton) findViewById(R.id.addQuickTextImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageEditText = (EditText) findViewById(R.id.quickTextEditText);
                if (messageEditText.getText().length() > 0){
                    quickTexts.add(messageEditText.getText().toString());
                    quickTextListAdapter.notifyDataSetChanged();
                    //TODO add to file system here
                    messageEditText.setText("");
                }
            }
        });
    }

    private void showQuickTextDialog(String message, final int i){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message)
                .setTitle(R.string.quickMessageDialog);

        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quickTexts.remove(i);
                quickTextListAdapter.notifyDataSetChanged();
                //TODO delete from file system here as well
            }
        });

        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quick_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Activity getActivity(){
        return this;
    }
}
