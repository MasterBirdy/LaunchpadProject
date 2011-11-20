package some.project.com;

import java.lang.reflect.Array;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);
  
        eventDay = (Spinner)findViewById(R.id.spinnerDay);

    	final ArrayAdapter<CharSequence> adapter2 = 
         		ArrayAdapter.createFromResource(this, 
         				R.array.day_array31, android.R.layout.simple_spinner_item);
         adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         eventDay.setAdapter(adapter2);
         eventDay.setOnItemSelectedListener(this);
        
        //this is to setup the spinner which is the drop down menu for
        //choosing the month for the event
        eventMonth = (Spinner)findViewById(R.id.spinnerMonth);
        //this adapter grabs the info from the strings.xml file
        ArrayAdapter<CharSequence> adapter = 
        		ArrayAdapter.createFromResource(this, 
        				R.array.month_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventMonth.setAdapter(adapter);
        
        
        //this adapter grabs the info from the strings.xml file
        eventDay = (Spinner)findViewById(R.id.spinnerDay);

        
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
        
        //From here down the information is retrieved from the input fields on the UI
        //use them to be input into the SQL database
        final EditText eventName = (EditText) findViewById(R.id.editText4);  
        String name = eventName.getText().toString();  
          
        final EditText locationName = (EditText) findViewById(R.id.editText3);  
        String location = locationName.getText().toString();  
          
        final EditText eventDescription = (EditText) findViewById(R.id.editText5);  
        String description = eventDescription.getText().toString();  
        
        final EditText startField = (EditText) findViewById(R.id.editText1);  
        String startTime = startField.getText().toString();  
          
        final EditText endField = (EditText) findViewById(R.id.editText2);  
        String endTime = endField.getText().toString();  
        
        final Spinner monthSpinner = (Spinner) findViewById(R.id.spinnerMonth);  
        String month = monthSpinner.getSelectedItem().toString(); 
        
        final Spinner daySpinner = (Spinner) findViewById(R.id.spinnerDay);  
        String day = daySpinner.getSelectedItem().toString(); 
        
        final Spinner yearSpinner = (Spinner) findViewById(R.id.spinnerYear);  
        String year = yearSpinner.getSelectedItem().toString(); 
        
        final CheckBox academicCheck = (CheckBox) findViewById(R.id.checkBox1);  
        boolean isAcademic = academicCheck.isChecked(); 
        
        final CheckBox socialCheck = (CheckBox) findViewById(R.id.checkBox2);  
        boolean isSocial = socialCheck.isChecked(); 
        
        final CheckBox professionalCheck = (CheckBox) findViewById(R.id.checkBox3);  
        boolean isProfessional = professionalCheck.isChecked(); 
        
        //problem with event constructor, type mismatch on some of the fields
        
       
        
        submitButton.setOnClickListener(this);
        //create Event object here and pass in the above stuff
        //this is the string for the event constructor but there are some mismatches
 
    	
    	eventMonth.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            	//addevent here

            }
            
            public void onNothingSelected(AdapterView<?> arg0) 
            {
            	//do nothing!!!
            }
        });

    }
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		//how do i use multiple spinners if the action only works for one ?
		//maybe a case statement using some sort of id and calling a method
		//accordingly 
		  Object selectedItem = parent.getSelectedItem();

		    // Do this if the first Spinner has a set of options that are
		    // known in advance.
	
		
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		//do nothing !!!
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(AddEvent.this, LaunchpadProjectActivity.class);
		startActivity(i);
			
	}

}
