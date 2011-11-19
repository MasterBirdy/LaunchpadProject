package some.project.com;

import java.util.Calendar;
import java.util.Locale;

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
    private Button yearButton;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         Calendar c = Calendar.getInstance(); 
         month = (Button) findViewById(R.id.button1);
         yearButton = (Button) findViewById(R.id.buttonYes);
         month.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US));
         yearButton.setText(c.get(Calendar.YEAR) + "");
     //   day.setText(c.get(Calendar.DAY_OF_MONTH));
       // year.setText(c.get(Calendar.YEAR));
        
        week1 = (Button)findViewById(R.id.button2);
        week1.setBackgroundColor(Color.BLUE);
  
    }
}