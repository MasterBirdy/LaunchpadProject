package some.project.com;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

public class AlternativeLayout extends ListActivity implements OnClickListener, OnItemSelectedListener {

	//Variables with info
	private ArrayList <Event> events;
	protected int day;
	protected int month;
	protected int year;
	protected Date currentDate;
	
	//Buttons
	private Spinner filterSpinner;
	private Button dateButton;
	private Button searchButton;
	private Button addButton;
	
	// stuff for detecting swipes
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	private OnTouchListener gestureListener;

	// xml animations files for making transitions
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	//ViewFlipper flips through two layouts in our code. It is how the dates smoothly transition
	private ViewFlipper viewFlipper;

	// these textfields will hold the information on the events

	private Calendar c = Calendar.getInstance();
	private DatePickerDialog.OnDateSetListener mDateSetListener;
	private SimpleDateFormat sdf;
	private SimpleDateFormat formatterForQuery;
    private static final int TIME_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID = 1;
	
    private ConnectivityManager connect;
    private NetworkInfo net;
    private AlertDialog alert;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  //boilerplate code
		
		setContentView(R.layout.alternativeui);
		
		filterSpinner = (Spinner) findViewById (R.id.filer_spinner);
		dateButton = (Button) findViewById (R.id.select_date);
		searchButton = (Button) findViewById (R.id.search_events);
		addButton = (Button) findViewById (R.id.add_event);
		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		
		
		//Initialize date values
		currentDate = c.getTime();
		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
	
		
		sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
		formatterForQuery = new SimpleDateFormat("yyyy-MM-dd");
		dateButton.setText(sdf.format(currentDate));
		
		fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
		
		mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year2, 
                                      int monthOfYear2, int dayOfMonth2) 
                {
					Log.v("Date", year2 + "");
					Log.v("Date", monthOfYear2 + "");
					Log.v("Date", dayOfMonth2 + "");
                	year = year2;
                	month = monthOfYear2;
                	day = dayOfMonth2;
                	currentDate = new Date(year, month, day) ;
                	updateEventList();
                	updateDateTitle();

                }
       };
       


		
		//Set listeners
		filterSpinner.setOnItemSelectedListener(this);
		dateButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		addButton.setOnClickListener(this);

		//loads xml animations
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
		

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
	
//	@Override
//	pub

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
					fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
				}
					//<- swipe
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					c.set(5,  c.get(Calendar.DATE)-1); //decrease day by one
					fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}

	@Override
    protected Dialog onCreateDialog(int id) {
         	return new DatePickerDialog(this,
         			mDateSetListener,
         			day, month, year);
    }
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
		((DatePickerDialog) dialog).updateDate(year, month, day);
    }    

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	public void onClick (View v)
	{
		switch (v.getId())
		{
			case R.id.select_date:
					showDialog(DATE_DIALOG_ID);
					fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
		            break;
			case R.id.search_events:
					Intent startSearch = new Intent(AlternativeLayout.this, SimpleEventListActivity.class);
					startActivity(startSearch);
				break;
			case R.id.add_event:
				Intent startAdd = new Intent (AlternativeLayout.this, AddEvent.class);
				startActivity(startAdd);
				break;
		}
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
    
    private void updateEventList ()
    {
    	
    }
    
    private void updateDateTitle ()
    {
    	dateButton.setText(sdf.format(currentDate));
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
    		stringEvents.add("There Are No Events for" + sdf.format(currentDate));
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
	

}

