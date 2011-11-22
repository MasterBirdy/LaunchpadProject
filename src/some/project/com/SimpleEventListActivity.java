package some.project.com;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SimpleEventListActivity extends ListActivity implements OnClickListener{

	private ArrayList <String> eventNames;
	private ArrayList <String> stringEvents;
	private EditText editText;
	private Button submitButton;
	private Button homeButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simpleeventlayout);
        editText = (EditText) findViewById (R.id.searchEditText);
        submitButton = (Button) findViewById (R.id.searchSubmitButton);
        homeButton = (Button) findViewById (R.id.searchHomeButton);
        submitButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        stringEvents = new ArrayList <String> ();
        eventNames = new ArrayList <String> ();
        fill();
    }


    private void fill() {
    	if(stringEvents.isEmpty())
    	{
    		eventNames.clear();
    		eventNames.add("No Results Matching " + editText.getText());
    	}
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventNames));
    }   

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	if (!stringEvents.isEmpty())
    	{
    		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

            alt_bld.setMessage(stringEvents.get(position)).setCancelable(false)
            	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int id) {
            			dialog.cancel();
            		}
           });
           AlertDialog alert = alt_bld.create();
           alert.setTitle("Event Details");
            alert.show();
    	}
    }


	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.searchSubmitButton:
				String query = editText.getText().toString();
				Log.v("PIZZA", query);
				ArrayList <Event> events = Event.searchEvents(query);
				Log.v("BUNNIES", ""+ events.size());
				stringEvents = new ArrayList <String> ();
				eventNames = new ArrayList <String> ();
				for (Event event: events)
				{
					String text = event.toString();
					stringEvents.add(text);
					eventNames.add(event.getName());
				}
				fill();
				break;
			case R.id.searchHomeButton:
				Intent i = new Intent (SimpleEventListActivity.this, LaunchpadProjectActivity.class);
				startActivity(i);
				break;
		}
		
	}
}
