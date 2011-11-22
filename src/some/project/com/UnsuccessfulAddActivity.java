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

public class UnsuccessfulAddActivity extends Activity implements OnClickListener
{

	private Button retryButton;
	private Button returnButton;
	private TextView textView;
	
	@Override
	public void onCreate (Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.unsuccessfuladd);
		Log.v("Successful Add Load", "In Successful Add");
		ArrayList <String> eventValues = (ArrayList <String>) getIntent().getExtras().get("eventValues");
		getIntent().removeExtra("eventValues");
		String output = getOutputString (eventValues);
		retryButton = (Button) findViewById(R.id.unsuccessfulAddRetryButton);
		retryButton.setOnClickListener(this);
		returnButton = (Button) findViewById(R.id.unsuccessfulAddReturnButton);
		returnButton.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.unsuccessfulAddTextView);
		textView.setText(output);
		
	}

	public void onClick(View arg0) {
		switch (arg0.getId())
		{
			case R.id.unsuccessfulAddReturnButton:
				Intent intent = new Intent (UnsuccessfulAddActivity.this, LaunchpadProjectActivity.class);
				startActivity(intent);
				break;
			case R.id.unsuccessfulAddRetryButton:
				Intent intent2 = new Intent (UnsuccessfulAddActivity.this, AddEvent.class);
				startActivity(intent2);
				break;
		}
	}
	
	//returns a cleanly formatted string to put into textview
	//for unsuccessful add it complains about every field that is null;
	private String getOutputString (ArrayList <String> eventValues)
	{
		String output = "You added an event with the following empty event details:\n";
		String newline = "\n";
		String [] labels = new String [] {"Name", "Date", "Start time", "End time", "Description",
				"Location", "Category"};
		for (int i = 0; i < 6; i++)
		{
			if (eventValues.get(i).isEmpty())
			{
				output += labels[i] + newline;
			}
		}
		if (eventValues.get(6).equals("0") && eventValues.get(7).equals("0") &&
				eventValues.get(8).equals("0"))
		{
			output += "Category (must check at least one option)" + newline;
		}
		return output;
	}
	
	
}
