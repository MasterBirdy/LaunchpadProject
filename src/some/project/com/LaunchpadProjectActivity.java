package some.project.com;

import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LaunchpadProjectActivity extends Activity {
	
	private TextView month;
	private TextView day;
	private TextView year;
	private Button week1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        month = (TextView) findViewById(R.id.month); 
        day = (TextView) findViewById(R.id.day); 
        year = (TextView) findViewById(R.id.month); 
         Calendar c = Calendar.getInstance(); 
         month.setText(c.get(Calendar.MONTH) + "");
     //   day.setText(c.get(Calendar.DAY_OF_MONTH));
       // year.setText(c.get(Calendar.YEAR));
        
        week1 = (Button)findViewById(R.id.button2);
        week1.setBackgroundColor(Color.BLUE);
  
    }
}