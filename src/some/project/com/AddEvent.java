package some.project.com;

import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEvent extends Activity implements OnItemSelectedListener {

	Spinner eventMonth;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
  
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
    }
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub

		Toast.makeText(parent.getContext(), 
				"The month is " + parent.getItemAtPosition(pos).toString(), 
				Toast.LENGTH_LONG).show();
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		//do nothing !!!!
		
	}
	


}
