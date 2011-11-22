package some.project.com;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LaunchpadProjectActivity extends ListActivity  implements OnClickListener, OnItemSelectedListener {

	private ArrayList<String> monthList = new ArrayList<String>(12);

	protected Date currentDate;
	private ArrayList <Event> events;

	private TextView textDate; // date text
	private TextView textDate1; // alternative layout date text
	private Button week1;
	private Button week2;
	private Button week3;
	private Button week4;
	private Button week5;
	private Button week6;
	private Button allButton;
	private Button aButton;
	private Button sButton;
	private Button pButton;
	private Button eventButton;
	private Button searchButton;

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

	private boolean isAlternativeLayoutOff = true; // checks to see if the 2nd layout in ViewFlipper is being used
	Calendar c = Calendar.getInstance(); 

	private ConnectivityManager connect;
	private NetworkInfo net;
	private AlertDialog alert;

	private Spinner monthSpinnerFront;
	private Spinner yearSpinnerFront;

	private SimpleDateFormat sdf;
	private SimpleDateFormat formatterForQuery;
	private boolean justStarted = true;


	//for the spinner
	private Integer countt;
	String y, currentM, currentD, currentY, newCurrentD;
	String x, currentyM, currentyD, currentyY, newCurrentyD;
	Integer q, z;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  //boilerplate code
		setContentView(R.layout.testlayout);

		monthList.add("January");
		monthList.add("February");
		monthList.add("March");
		monthList.add("April");
		monthList.add("May");
		monthList.add("June");
		monthList.add("July");
		monthList.add("August");
		monthList.add("September");
		monthList.add("October");
		monthList.add("November");
		monthList.add("December");

		if (!isOnline())
		{
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage("Error: Must have network connection");
			build.setCancelable(false);
			build.setPositiveButton("Close", new DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which){
					LaunchpadProjectActivity.this.finish();
				}
			});

			AlertDialog alert = build.create();
			alert.show();
		}

		textDate = (TextView) findViewById(R.id.date);
		textDate1 = (TextView) findViewById(R.id.date1);
		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		week1 = (Button)findViewById(R.id.button2);
		week2 = (Button)findViewById(R.id.button3);
		week3 = (Button)findViewById(R.id.button4);
		week4 = (Button)findViewById(R.id.button5);
		week5 = (Button)findViewById(R.id.button6);
		week6 = (Button)findViewById(R.id.button6TWO);
		allButton = (Button)findViewById(R.id.button7);
		aButton=(Button)findViewById(R.id.button8);
		sButton=(Button)findViewById(R.id.button10);
		pButton=(Button)findViewById(R.id.button9);
		monthSpinnerFront = (Spinner)findViewById(R.id.spinnerMM);
		yearSpinnerFront = (Spinner)findViewById(R.id.spinnerFYear);
		eventButton = (Button)findViewById(R.id.button11);
		searchButton = (Button)findViewById(R.id.button12);

		currentDate = c.getTime();

		sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
		formatterForQuery = new SimpleDateFormat("yyyy-MM-dd");
		fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));

		//monthButton.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US)); // sets the month button to the current month
		//yearButton.setText(c.get(Calendar.YEAR) + ""); // year button same thing
		textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));

		//loads xml animations
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

		week1.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				c.set(Calendar.DATE, 1);
				updateCurrentLayout();
			}
		});

		week2.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				c.set(Calendar.WEEK_OF_MONTH, 2);
				c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				updateCurrentLayout();
			}
		});
		week3.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				c.set(Calendar.WEEK_OF_MONTH, 3);
				c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				updateCurrentLayout();
			}
		});
		week4.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				c.set(Calendar.WEEK_OF_MONTH, 4);
				c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				updateCurrentLayout();
			}
		});
		week5.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Calendar c1 = Calendar.getInstance(); 
				c1.set(Calendar.WEEK_OF_MONTH, 5);
				c1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				if (c.get(Calendar.MONTH) != c1.get(Calendar.MONTH))
					Toast.makeText(getApplicationContext(), "This month does not have a week 5.", Toast.LENGTH_LONG);
				else {
					c.set(Calendar.WEEK_OF_MONTH, 5);
					c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					updateCurrentLayout();
				}
			}
		});
		week6.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Calendar c1 = Calendar.getInstance(); 
				c1.set(Calendar.WEEK_OF_MONTH, 6);
				c1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				if (c.get(Calendar.MONTH) != c1.get(Calendar.MONTH))
					Toast.makeText(getApplicationContext(), "This month does not have a week 6.", Toast.LENGTH_LONG);
				else {
					c.set(Calendar.WEEK_OF_MONTH, 6);
					c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					updateCurrentLayout();
				}
			}
		});

		// goes to addEvent activity
		eventButton.setOnClickListener(new View.OnClickListener(){
			public void onClick( View v) {
				Intent i = new Intent();
				i.setClassName("some.project.com",
						"some.project.com.AddEvent");
				startActivity(i);
			}
		});
		searchButton.setOnClickListener(new View.OnClickListener(){
			public void onClick( View v) {
				Intent i = new Intent();
				i.setClassName("some.project.com",
						"some.project.com.SimpleEventListActivity");
				startActivity(i);
			}
		});
		allButton.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v) {
					fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
				}
			});
		aButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				fillList (Event.getEventsOnDate(formatterForQuery.format(currentDate), true, false, false));
			}
		});
		pButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				fillList (Event.getEventsOnDate(formatterForQuery.format(currentDate), false, true, false));
			}
		});
		sButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				fillList (Event.getEventsOnDate(formatterForQuery.format(currentDate), false, false, true));
			}
		});
		
		
		

		//HERE IS WHERE THE SPINNERS ARE DEFINED!!!!!!!!!!!!!!!!!!!!!!!
		ArrayAdapter<CharSequence> adapter1 = 
				ArrayAdapter.createFromResource(this, 
						R.array.month_array, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSpinnerFront.setAdapter(adapter1);

		ArrayAdapter<CharSequence> adapter2 = 
				ArrayAdapter.createFromResource(this, 
						R.array.year_array, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearSpinnerFront.setAdapter(adapter2);

		//I ADD THIS TO KEEP TRACK OF THE NUMBER OF TIMES THE SPINNER HAVE BEEN USED

		//HERE IS THE STUFF THAT UPDATES THE TEXT THAT SHOWS THE CURRENT DATE!!!!!!!!!!!!!
		
		yearSpinnerFront.setOnItemSelectedListener(new OnItemSelectedListener() { 
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
					long id) {
				if (!justStarted){
				//sets it to current date, as done in above code
					// this gets the new selected year and updates that but maintains the previous day and month
			     	String year = yearSpinnerFront.getSelectedItem().toString();
					Integer y = Integer.parseInt(year);
					c.set(Calendar.YEAR, y);
					updateCurrentLayout();
				}

					//textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
			}

			public void onNothingSelected(AdapterView<?> paretn)
			{
			}});

		//HERE IS MORE STUFF THAT UPDATES THE CURRENT DATE!!!!!!!!!!!!!!
		monthSpinnerFront.setOnItemSelectedListener(new OnItemSelectedListener() { 
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
					long id) {
				if (!justStarted){
					// this gets the new selected month and updates that but maintains the previous day and year
					String month = monthSpinnerFront.getSelectedItem().toString(); 
					int i = monthList.indexOf(month);
					c.set(Calendar.MONTH, i);
					updateCurrentLayout();
				}


					//textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
			}

			public void onNothingSelected(AdapterView<?> paretn)
			{
			}});

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
					updateWhichLayout(); 
					if(isAlternativeLayoutOff)
						isAlternativeLayoutOff = false;
					else 
						isAlternativeLayoutOff = true;
				}
				//<- swipe
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					c.set(5,  c.get(Calendar.DATE)-1); //decrease day by one
					updateWhichLayout();
					if(isAlternativeLayoutOff)
						isAlternativeLayoutOff = false;
					else 
						isAlternativeLayoutOff = true;
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


	private void updateWhichLayout(){
		if(isAlternativeLayoutOff){
			textDate1.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
			currentDate = c.getTime();
			fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
			updateSpinners();
		}
		else {
			textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
			currentDate = c.getTime();
			fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
			updateSpinners();
		}
	}

	private void updateCurrentLayout(){
		if(!isAlternativeLayoutOff){
			textDate1.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
			currentDate = c.getTime();
			fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
			updateSpinners();
		}
		else {
			textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
			currentDate = c.getTime();
			fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
			updateSpinners();
		}
	}

	public boolean isOnline()
	{
		final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}


	private void fillList(ArrayList <Event> eventsToFill) {
		events = eventsToFill;
		ArrayList <String> stringEvents = new ArrayList <String> ();
		for (Event event: eventsToFill)
		{
			stringEvents.add(event.getName());
		}
		if (eventsToFill.isEmpty())
		{
			stringEvents.add("There Are No Events for " + sdf.format(currentDate));
		}
		if (eventsToFill.size() < 10)
			for (int i = 0; i < 10 - eventsToFill.size() ; i ++)
				stringEvents.add("\n");
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringEvents));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		if (position < events.size())
		{
			alt_bld.setMessage(events.get(position).toString()).setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = alt_bld.create();
			alert.setTitle("Event Details");
			alert.show();
		}
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
	
	private void updateSpinners(){
		if(monthSpinnerFront.getSelectedItemPosition() != c.get(Calendar.MONTH))
			monthSpinnerFront.setSelection(1);
		if(yearSpinnerFront.getSelectedItemPosition() != c.get(Calendar.YEAR))
			yearSpinnerFront.setSelection(c.get(Calendar.YEAR));
		}
		
}

