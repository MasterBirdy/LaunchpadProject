package some.project.com;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
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
import android.widget.Toast;

public class AddEvent extends Activity implements OnItemSelectedListener, OnClickListener{

	Spinner eventMonth;
	Spinner eventDay;
	Spinner eventYear;
	Button submitButton;
	Date newDate;
	Event add;
	ArrayList<String> addForEvent;
	
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
        
        
        //this adapter grabs the info from the strings.xml file
        
        eventYear = (Spinner)findViewById(R.id.spinnerYear);
        //this adapter grabs the info from the strings.xml file
        ArrayAdapter<CharSequence> adapter3 = 
        		ArrayAdapter.createFromResource(this, 
        				R.array.year_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventYear.setAdapter(adapter3);
        eventYear.setOnItemSelectedListener(this);
        
        //here is the button listener that will submit the info to the SQL database
        //in the onClick method is where the Event class will be used
        submitButton = (Button)findViewById(R.id.buttonSendFeedback);
        
        submitButton.setOnClickListener(this);
        //create Event object here and pass in the above stuff
        //this is the string for the event constructor but there are some mismatches


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
		//addevent here
        //add.addEvent(addForEvent);
		final EditText eventName = (EditText) findViewById(R.id.editText4);  
        String name = eventName.getText().toString(); 
        
        final EditText locationName = (EditText) findViewById(R.id.editText3);  
        String location = locationName.getText().toString();  
          
        final EditText eventDescription = (EditText) findViewById(R.id.editText5);  
        String description = eventDescription.getText().toString();  
        
        final EditText startField = (EditText) findViewById(R.id.editText1);  
        String startTime = startField.getText().toString();  
        Integer start = Integer.parseInt(startTime);
          
        final EditText endField = (EditText) findViewById(R.id.editText2);  
        String endTime = endField.getText().toString();  
        Integer end = Integer.parseInt(endTime);
        
        //month number might be plus one
        final Spinner monthSpinner = (Spinner) findViewById(R.id.spinnerMonth);  
        String month = monthSpinner.getSelectedItem().toString(); 
        Integer monthNumber = eventMonth.getSelectedItemPosition();
        
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
        
        String date = month.concat(day).concat(year);
        Date dateC = new Date();
        dateC.setDate(dayNumber);
        dateC.setMonth(monthNumber);
        dateC.setYear(yearNumber);
        
        Log.v(name, "START TIME HEREEEEEEEEEEEEEEEE");
        
        ArrayList<String> addForEvent = new ArrayList<String>();
        addForEvent.add(name);
        addForEvent.add(date);
        addForEvent.add(startTime);
        addForEvent.add(endTime);
        addForEvent.add(description);
        addForEvent.add(location);
        addForEvent.add(academic);
        addForEvent.add(social);
        addForEvent.add(professional);
        
        add = new Event(name, start, end, description, dateC, isAcademic, isSocial, isProfessional, location);
        
        //add.addEvent(addForEvent);
        
		Intent i = new Intent(AddEvent.this, LaunchpadProjectActivity.class);
		startActivity(i);
			
	}

}
