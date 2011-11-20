package some.project.com;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEvent extends Activity implements OnItemSelectedListener {

	Spinner eventMonth;
	Spinner eventDay;
	Spinner eventYear;
	
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
        eventMonth.setOnItemSelectedListener(new AddEvent());
        
        eventDay = (Spinner)findViewById(R.id.spinnerDay);
        //this adapter grabs the info from the strings.xml file
    
        	 ArrayAdapter<CharSequence> adapter2 = 
             		ArrayAdapter.createFromResource(this, R.array.day_array31, android.R.layout.simple_spinner_item);
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             eventDay.setAdapter(adapter2);
             eventDay.setOnItemSelectedListener(new AddEvent());
        
    
        
        eventYear = (Spinner)findViewById(R.id.spinnerDay);
        //this adapter grabs the info from the strings.xml file
        ArrayAdapter<CharSequence> adapter3 = 
        		ArrayAdapter.createFromResource(this, 
        				R.array.year_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventYear.setAdapter(adapter3);
        eventYear.setOnItemSelectedListener(new AddEvent()); 
    }
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		//how do i use multiple spinners if the action only works for one ?
		//maybe a case statement using some sort of id and calling a method
		//accordingly 
		switch (parent.getId()) {
	    case R.id.spinnerMonth:
	    	Toast.makeText(parent.getContext(), 
					"The month is " + parent.getItemAtPosition(pos).toString(), 
					Toast.LENGTH_LONG).show();	 
	    	
	    case R.id.spinnerDay:
	        
	        break;
		}	
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		//do nothing !!!!
		
	}
	


}
