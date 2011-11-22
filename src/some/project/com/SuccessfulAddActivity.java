package some.project.com;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SuccessfulAddActivity extends Activity implements OnClickListener
{

	private Button returnButton;
	private TextView textView;
	
	@Override
	public void onCreate (Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.successfuladd);

		Log.v("Successful Add Load", "In Successful Add");
		ArrayList <String> eventValues = (ArrayList <String>) getIntent().getExtras().get("eventValues");
		getIntent().removeExtra("eventValues");
		String output = getOutputString (eventValues);
		returnButton = (Button) findViewById(R.id.successfulAddReturnButton);
		returnButton.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.successfulAddTextView);
		textView.setText(output);
		
	}

	public void onClick(View arg0) {
		Intent intent = new Intent (SuccessfulAddActivity.this, LaunchpadProjectActivity.class);
		startActivity(intent);
	}
	
	//returns a cleanly formatted string to put into textview
	private String getOutputString (ArrayList <String> eventValues)
	{
		String output = "You added an event with the following details:\n";
		String newline = "\n";
		output += "Name = " + eventValues.get(0);
		output += newline;
		output += "Date = " + eventValues.get(1);
		output += newline;
		output += "Start time = " + eventValues.get(2);
		output += newline;
		output += "End time = " + eventValues.get(3);
		output += newline;
		output += "Description = " + eventValues.get(4);
		output += newline;
		output += "Location = " + eventValues.get(5);
		output += newline;
		if (eventValues.get(6).equals("1"))
			output += "Event is academic" + newline;
		if (eventValues.get(7).equals("1"))
			output += "Event is social" + newline;
		if (eventValues.get(8).equals("1"))
			output += "Event is professional" + newline;
		return output;
	}
	
	
}
