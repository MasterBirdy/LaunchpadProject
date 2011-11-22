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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

public class ModernLayout extends ListActivity implements OnClickListener, OnItemSelectedListener {

	private static final int RESULT_CODE = 0;
	private static final String FILTER_ALL = "Show All Events";
	private static final String FILTER_ACADEMIC = "Show Academic Events";
	private static final String FILTER_PROFESSIONAL = "Show Professional Events";
	private static final String FILTER_SOCIAL = "Show Social Events";
	
	//Variables with info
	private ArrayList <Event> events;
	protected int day;
	protected int month;
	protected int year;
	protected Date currentDate;
	private boolean showAll;
	private boolean showAcademic;
	private boolean showProfessional;
	private boolean showSocial;
	
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
    private static final int DATE_DIALOG_ID = 1;
	
    private ConnectivityManager connect;
    private NetworkInfo net;
    private AlertDialog alert;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  //boilerplate code
		
		setContentView(R.layout.modern_layout);
		
		filterSpinner = (Spinner) findViewById (R.id.filer_spinner);
		ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, 
			R.array.filter_array, android.R.layout.simple_spinner_item);
		filterSpinner.setAdapter(adapter);
		
		dateButton = (Button) findViewById (R.id.select_date);
		searchButton = (Button) findViewById (R.id.search_events);
		addButton = (Button) findViewById (R.id.add_event);
		
		//Initialize conditions
		showAll = true;
		showAcademic = false;
		showProfessional = false;
		showSocial = false;
		
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
                	year = year2;
                	month = monthOfYear2;
                	day = dayOfMonth2;
                	c.set(year, month, day);
                	currentDate = c.getTime();
                	updateUI();

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
		
		if (!isOnline())
        {
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setMessage("Error: Must have network connection");
            build.setCancelable(false);
            build.setPositiveButton("Close", new DialogInterface.OnClickListener(){
               
                public void onClick(DialogInterface dialog, int which){
                    ModernLayout.this.finish();
                }
            });
           
            AlertDialog alert = build.create();
            alert.show();
        }
		
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
				}
					//<- swipe
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					c.set(5,  c.get(Calendar.DATE)-1); //decrease day by one
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
				if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ECLAIR_MR1)
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(this);

					alert.setTitle("Enter Date");
					alert.setMessage("Enter Date in YYYY-MM-DD Format");

					// Set an EditText view to get user input 
					final EditText input = new EditText(this);
					alert.setView(input);

					alert.setPositiveButton("Go To Date", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String value = input.getText().toString();
							String [] parts = value.split("-");
							year = Integer.parseInt (parts[0]);
		                	month = Integer.parseInt(parts[1])-1;
		                	day = Integer.parseInt(parts[2])-1;
		                	currentDate = new Date( year, month, day) ;
		                	fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
		                	updateUI();
					  	}
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}
					});
					AlertDialog dialog = alert.create();
					dialog.show();
					
				}
				else
				{
					showDialog(DATE_DIALOG_ID);
					fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
					updateUI();
				}
				break;
			case R.id.search_events:
					Intent startSearch = new Intent(ModernLayout.this, SimpleEventListActivity.class);
					startActivityForResult(startSearch, RESULT_CODE);
				break;
			case R.id.add_event:
				Intent startAdd = new Intent (ModernLayout.this, AddEvent.class);
				startActivityForResult(startAdd, RESULT_CODE);
				break;
		}
		updateUI();
	}
	
    protected void onActivityResult(int requestCode, int resultCode,
    		Intent data) {
        if (requestCode == RESULT_CODE) {
        	updateUI();
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
    	ArrayList <Event> matchingEventsToFill = eventsToFill;
    	if (!showAll)
    	{
    		matchingEventsToFill= new ArrayList <Event> ();
    		for (Event e: eventsToFill)
    		{
    			if (showAcademic && e.isAcademic())
    				matchingEventsToFill.add(e);
    			if (showProfessional && e.isProfessional())
    				matchingEventsToFill.add(e);
    			if (showSocial && e.isSocial())
    				matchingEventsToFill.add(e);
    		}
    	}
    	
    	events = matchingEventsToFill;
    	ArrayList <String> stringEvents = new ArrayList <String> ();
    	for (Event event: events)
    	{
    		stringEvents.add(event.getName());
    	}
    	if (events.isEmpty())
    	{
    		stringEvents.add("There Are No Matching Events for " + sdf.format(currentDate));
    	}
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
    
    private void updateUI()
    {
    	//update date
		dateButton.setText(sdf.format(currentDate));
    	//update list of events
		fillList (Event.getMatchingEvents("Date", formatterForQuery.format(currentDate)));
    	
    }
    
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String currentF = filterSpinner.getSelectedItem().toString();
		if (currentF.equals(FILTER_ALL))
		{
			showAll = true;
			showAcademic = false;
			showProfessional = false;
			showSocial = false;
		}
		else if (currentF.equals(FILTER_ACADEMIC))
		{
			showAll = false;
			showAcademic = true;
			showProfessional = false;
			showSocial = false;
		}
		else if (currentF.equals(FILTER_SOCIAL))
		{
			showAll = false;
			showAcademic = false;
			showProfessional = false;
			showSocial = true;
		}
		else if (currentF.equals(FILTER_PROFESSIONAL))
		{
			showAll = false;
			showAcademic = false;
			showProfessional = true;
			showSocial = false;
		}
		Log.v("Values", showAll + " " + showAcademic + " " + showProfessional + " " + showSocial);
		updateUI();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}

