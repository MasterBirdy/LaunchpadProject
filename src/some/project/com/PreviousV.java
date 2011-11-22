package some.project.com;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreviousV extends Activity {

	Button classic;
	Button modern;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		classic  = (Button)findViewById(R.id.buttonClassic);
		modern = (Button)findViewById(R.id.buttonModern);
		
		classic.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent i = new Intent(PreviousV.this, LaunchpadProjectActivity.class);
				startActivity(i);				
			}
		});
		
		//change this one send user to Ashwins UI
		modern.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent i = new Intent(PreviousV.this, ModernLayout.class);
				startActivity(i);				
			}
		});
		
		
		
	}

	 

}
