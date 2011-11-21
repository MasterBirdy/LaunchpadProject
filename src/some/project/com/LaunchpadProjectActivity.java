package some.project.com;

import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LaunchpadProjectActivity extends Activity {

	private TextView textDate; // date text
	private TextView textDate1; // alternative layout date text
	private Button week1;
	private Button yearButton; 
	private Button monthButton;

	// stuff for detecting swipes
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	// xml animations files for making transitions
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	//ViewFlipper flips through two layouts in our code. It is how the dates smoothly transition
	private ViewFlipper viewFlipper;

	// these textfields will hold the information on the events
	private EditText editText1;
	private EditText editText2;

	private boolean isAlternativeLayoutOff = true; // checks to see if the 2nd layout in ViewFlipper is being used
	Calendar c = Calendar.getInstance(); 
	
    private ConnectivityManager connect;
    private NetworkInfo net;
    private AlertDialog alert;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  //boilerplate code
		setContentView(R.layout.main);
		
	     if (!isOnline())
	        {
	        	/*alert = new AlertDialog.Builder(getApplicationContext()).create();
	        	alert.setTitle("Error");
	        	alert.setMessage("No network connection");
	        	alert.setButton("Close", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
	        	alert.show();*/
	    	 	Toast.makeText(getApplicationContext(), "This doesn't work!", Toast.LENGTH_LONG);
	        	//this.finish();
	        }

		monthButton = (Button) findViewById(R.id.button1);         // R.id.findStuff
		yearButton = (Button) findViewById(R.id.buttonYes);
		textDate = (TextView) findViewById(R.id.date);
		textDate1 = (TextView) findViewById(R.id.date1);
		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		week1 = (Button)findViewById(R.id.button2);
		editText1 = (EditText) findViewById(R.id.editTextNotAlt);
		editText2 = (EditText) findViewById(R.id.editTextAlt);

		monthButton.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US)); // sets the month button to the current month
		yearButton.setText(c.get(Calendar.YEAR) + ""); // year button same thing
		textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));

		editText1.setFocusable(false); // makes it so you can't edit the text fields
		editText2.setFocusable(false);
		editText1.setText(returnEvents());

		//loads xml animations
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

		// goes to addEvent activity
		monthButton.setOnClickListener(new View.OnClickListener(){
			public void onClick( View v) {
				Intent i = new Intent();
				i.setClassName("some.project.com",
						"some.project.com.AddEvent");
				startActivity(i);
			}
		});

		//detects for swipes
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;

				// -> swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
					c.set(5,  c.get(Calendar.DATE)+1); // increase day by one

					if(isAlternativeLayoutOff){ // if 1st layout is being used
						textDate1.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
						editText2.setText(returnEvents());
						isAlternativeLayoutOff = false;

					} else { //if 2nd layout is being used
						textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
						editText1.setText(returnEvents());
						isAlternativeLayoutOff = true;
					}
					//<- swipe
				}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					c.set(5,  c.get(Calendar.DATE)-1); //decrease day by one

					if(isAlternativeLayoutOff){
						textDate1.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
						editText2.setText(returnEvents());
						isAlternativeLayoutOff = false;

					} else {
						textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
						editText1.setText(returnEvents());
						isAlternativeLayoutOff = true;
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	private String returnDate(int year, int month, int day){ //formats date for sql
		return year + "-" + month + "-" + day;
	}

	private String returnEvents(){

		//if (Event.getMatchingEvents("Date", returnDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE))).get(0).getName() == null)
		return "This is a test";
		//else return Event.getMatchingEvents("Date", returnDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE))).get(0).getName();
	}
	
    public boolean isOnline()
    {
    	final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
    	if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {
    		return true;
    	} else {
    		return false;
    	}
    }
}

