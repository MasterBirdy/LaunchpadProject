package some.project.com;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddEventModern extends Activity implements OnItemSelectedListener, OnClickListener{

	Spinner eventMonth;
	Spinner eventDay;
	Spinner eventYear;
	Spinner sTime;
	Spinner eTime;
	Button submitButton;
	Date newDate;
	Event add;
	ArrayList<String> addForEvent;
	boolean timeFixed = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent);

		//this is to setup the spinner which is the drop down menu for
		//choosing the month for the event
		eventMonth = (Spinner)findViewById(R.id.spinnerMonth);
		//this adapter grabs the info from the strings.xml file
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(this, 
						R.array.month_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventMonth.setAdapter(adapter);
		eventMonth.setOnItemSelectedListener(this);


		eventDay = (Spinner)findViewById(R.id.spinnerDay);

		ArrayAdapter<CharSequence> adapter2 = 
				ArrayAdapter.createFromResource(this, 
						R.array.day_array31, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventDay.setAdapter(adapter2);
		eventDay.setOnItemSelectedListener(this);

		eventYear = (Spinner)findViewById(R.id.spinnerYear);
		//this adapter grabs the info from the strings.xml file
		ArrayAdapter<CharSequence> adapter3 = 
				ArrayAdapter.createFromResource(this, 
						R.array.year_array, android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventYear.setAdapter(adapter3);
		eventYear.setOnItemSelectedListener(this);

		//start time Spinner
		sTime = (Spinner)findViewById(R.id.spinnerSTime);
		ArrayAdapter<CharSequence> adapter4 = 
				ArrayAdapter.createFromResource(this, 
						R.array.startTime_array, android.R.layout.simple_spinner_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sTime.setAdapter(adapter4);
		sTime.setOnItemSelectedListener(this);

		eTime = (Spinner)findViewById(R.id.spinnerETime);
		ArrayAdapter<CharSequence> adapter5 = 
				ArrayAdapter.createFromResource(this, 
						R.array.eTime_array, android.R.layout.simple_spinner_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_item);
		eTime.setAdapter(adapter5);
		eTime.setOnItemSelectedListener(this);

		//here is the button listener that will submit the info to the SQL database
		//in the onClick method is where the Event class will be used
		submitButton = (Button)findViewById(R.id.buttonSendFeedback);

		submitButton.setOnClickListener(this);

	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		//how do i use multiple spinners if the action only works for one ?
		//maybe a case statement using some sort of id and calling a method
		//accordingly 

		// Do this if the first Spinner has a set of options that are
		// known in advance.


	}

	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		//do nothing !!!

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		final EditText eventName = (EditText) findViewById(R.id.editText4);  
		String name = eventName.getText().toString(); 

		final EditText locationName = (EditText) findViewById(R.id.editText3);  
		String location = locationName.getText().toString();  

		final EditText eventDescription = (EditText) findViewById(R.id.editText5);  
		String description = eventDescription.getText().toString();  

		String startTime = sTime.getSelectedItem().toString();
		String endTime = eTime.getSelectedItem().toString();


		//month number might be plus one
		final Spinner monthSpinner = (Spinner) findViewById(R.id.spinnerMonth);  
		String month = "" + monthSpinner.getSelectedItemPosition() + 1;

		final Spinner daySpinner = (Spinner) findViewById(R.id.spinnerDay);  
		String day = daySpinner.getSelectedItem().toString(); 
		Integer dayNumber = Integer.parseInt(day);

		final Spinner yearSpinner = (Spinner) findViewById(R.id.spinnerYear);  
		String year = yearSpinner.getSelectedItem().toString(); 
		Integer yearNumber = Integer.parseInt(year);

		final CheckBox academicCheck = (CheckBox) findViewById(R.id.checkBox1);  
		boolean isAcademic = academicCheck.isChecked(); 
		String academic;
		if(isAcademic == true)
		{
			academic = "1";
		}
		else
		{
			academic = "0";	
		}

		final CheckBox socialCheck = (CheckBox) findViewById(R.id.checkBox2);  
		boolean isSocial = socialCheck.isChecked(); 
		String social;
		if(isSocial == true)
		{
			social = "1";
		}
		else
		{
			social = "0";	
		}

		final CheckBox professionalCheck = (CheckBox) findViewById(R.id.checkBox3);  
		boolean isProfessional = professionalCheck.isChecked(); 
		String professional;
		if(isProfessional == true)
		{
			professional = "1";
		}
		else
		{
			professional = "0";	
		}

		//creating date object for arrayList
		String date = yearNumber + "-" + month + "-" + dayNumber;

		//Parse the input times (1:30 am into 24 hr time -> 013000
		boolean startTimeIsPm = startTime.endsWith("pm");
		startTime = startTime.replace("pm", "");
		startTime = startTime.replace("am", "");
		startTime = startTime.replace(":", "");
		int startTimeInt = Integer.parseInt(startTime);
		if (startTimeIsPm)
		{
			startTime += 12;
		}
		if (startTimeInt < 10)
		{
			startTime = "0" + startTime;
		}
		else
		{
			startTime = "" + startTime;
		}
		startTime += "00";

		boolean endTimeIsPm = endTime.endsWith("pm");
		endTime = endTime.replace("pm", "");
		endTime = endTime.replace("am", "");
		endTime = endTime.replace(":", "");
		int endTimeInt = Integer.parseInt(endTime);
		if (endTimeIsPm)
		{
			endTime += 12;
		}
		if (endTimeInt < 10)
		{
			endTime = "0" + endTime;
		}
		else
		{
			endTime = "" + endTime;
		}
		endTime += "00";
		//these log files verify the information
		//remember what was entered and check with the
		//log cat
		String LOG_TAG = "CheckThisPlease";
		Log.v(LOG_TAG, name);
		Log.v(LOG_TAG, description);
		Log.v(LOG_TAG, startTime);
		Log.v(LOG_TAG, endTime);
		Log.v(LOG_TAG, professional);
		Log.v(LOG_TAG, academic);
		Log.v(LOG_TAG, social);
		Log.v(LOG_TAG, location);
		Log.v("Date", date);

		timeFixed = false;

		if(startTimeIsPm == false && endTimeIsPm == false || startTimeIsPm == true && endTimeIsPm == true)

		{

			if(startTimeInt >= endTimeInt)

			{

				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

				alt_bld.setMessage("The Start time cannot come after the End Time") 
			       .setCancelable(false) 
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			           public void onClick(DialogInterface dialog, int id) { 
			                dialog.cancel();
			           } 

				});

				AlertDialog alert = alt_bld.create();
				alert.setTitle("Check Times Please");

				alert.show();
				timeFixed = false;
			}

		}

		else if(startTimeIsPm == true && endTimeIsPm == false){

			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

			alt_bld.setMessage("The Start time cannot come after the End Time") 
		       .setCancelable(false) 
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		           public void onClick(DialogInterface dialog, int id) { 
		                dialog.cancel();
		           } 

			});

			AlertDialog alert = alt_bld.create();
			alert.setTitle("Check Times Please");

			alert.show();
			timeFixed = false;

		}

		else

		{

			timeFixed = true;

		}

		if(timeFixed == true)

		{

			timeFixed = false;

			Intent i = new Intent(AddEventModern.this, LaunchpadProjectActivity.class);

			startActivity(i);

		}
		//the arrayList for the addEventMethod
		addForEvent = new ArrayList<String>();

		addForEvent.add(name);
		addForEvent.add(date);
		addForEvent.add(startTime);
		addForEvent.add(endTime);
		addForEvent.add(description);
		addForEvent.add(location);
		addForEvent.add(academic);
		addForEvent.add(social);
		addForEvent.add(professional);

		boolean eventAdded = Event.addEvent(addForEvent);
		if (!eventAdded)
		{
			TextView tv = new TextView (this);
			tv.setText("Please ensure all fields are filled in");
			setContentView (tv);
		}

		//addEvent causes crash
		//add.addEvent(addForEvent);

		Intent i = new Intent(AddEventModern.this, LaunchpadProjectActivity.class);
		startActivity(i);

	}

	public static String removeCharAt(String s, int pos) {
		StringBuffer buf = new StringBuffer( s.length() - 1 );
		buf.append( s.substring(0,pos) ).append( s.substring(pos+1) );
		return buf.toString();
	}

}
