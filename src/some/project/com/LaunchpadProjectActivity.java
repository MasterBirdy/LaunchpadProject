package some.project.com;

import java.util.Calendar;
import java.util.Locale;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LaunchpadProjectActivity extends Activity {
	
	private String month;
	private String day;
	private String year;
	private TextView textDate;
	private TextView textDate1;
	private Button week1;
    private Button yearButton;
    private Button monthButton;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
    private Animation slideRightOut;
    private ViewFlipper viewFlipper;
    private EditText editText1;
    Calendar c = Calendar.getInstance(); 
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         monthButton = (Button) findViewById(R.id.button1);
         yearButton = (Button) findViewById(R.id.buttonYes);
         textDate = (TextView) findViewById(R.id.date);
         textDate1 = (TextView) findViewById(R.id.date1);
         viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
         monthButton.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US));
         yearButton.setText(c.get(Calendar.YEAR) + "");
        textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
        week1 = (Button)findViewById(R.id.button2);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText1.setFocusable(false);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        monthButton.setOnClickListener(new View.OnClickListener(){
        	public void onClick( View v) {
        		Intent i = new Intent();
        		i.setClassName("some.project.com",
        		"some.project.com.AddEvent");
        		startActivity(i);
        	}
        });
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
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation(slideLeftIn);
                    viewFlipper.setOutAnimation(slideLeftOut);
                	viewFlipper.showNext();
                	c.set(5,  c.get(Calendar.DATE)+1);
                	 textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
                	 textDate1.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation(slideRightIn);
                    viewFlipper.setOutAnimation(slideRightOut);
                	viewFlipper.showPrevious();
                	c.set(5,  c.get(Calendar.DATE)-1);
                	 textDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
                	 textDate1.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.US) + " " + c.get(Calendar.DATE) + ", " +c.get(Calendar.YEAR));
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

}