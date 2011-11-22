package some.project.com;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;


public class Event 
{

	private static final String EVENT_DELIMITER="NEXT_EVENT"; //String that delimits individual events 
	private static final String EVENT_PART_DELIMITER="##";
	
	private String name;
	private int startTime;
	private int endTime;
	private String description;
	private String location;
	private Date date;
	private boolean academic;
	private boolean social;
	private boolean professional;
	
	public Event (String name, int startTime, int endTime, String description,
			Date date, boolean academic, boolean social, boolean professional,
			String location)
	{
		this.name = name;
		this.startTime=startTime;
		this.endTime=endTime;
		this.description=description;
		this.date=date;
		this.academic=academic;
		this.social=social;
		this.professional=professional;
		this.location = location;
	}
	
	public Event (String [] eventParts)
	{
		this.name = eventParts[0];
		this.startTime = Integer.parseInt(eventParts[2].replaceAll(":", ""));
		this.endTime = Integer.parseInt(eventParts[3].replaceAll(":", ""));
		String [] dateParts = eventParts[1].split("-");
		this.date = new Date (Integer.parseInt(dateParts[0]), 
				Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
		this.description = eventParts[4];
		this.location = eventParts[5];
		this.academic = false;
		if (eventParts[6].equals("1"))
			academic = true;
		this.social = false;
		if (eventParts[7].equals("1"))
			social = true;
		this.professional = false;
		if (eventParts[8].equals("1"))
			professional = true;
		
	}
	
	public void setProfessional(boolean professional) {
		this.professional = professional;
	}
	public boolean isProfessional() {
		return professional;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setAcademic(boolean academic) {
		this.academic = academic;
	}
	public boolean isAcademic() {
		return academic;
	}
	public void setSocial(boolean social) {
		this.social = social;
	}
	public boolean isSocial() {
		return social;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getStartTime() {
		return startTime;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
	
	
	public static ArrayList <Event> getMatchingEvents (String category, String categoryValue)
	{
		String urlValue = "http://team1.appjam.roboteater.com/selecter.php?";
		urlValue += "Category=" + category + "&" + category + "=" + categoryValue;
		ServerEventTask set = new ServerEventTask();
		set.execute(new String [] {urlValue});	
		String result = "";
		try {
			result = set.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		return parseString (result);
	}
	
	public static ArrayList <Event> getEventsOnDate (String date, boolean academic, boolean professional, boolean social)
	{
		ArrayList <Event> eventsOnDate = Event.getMatchingEvents("Date", date);
		ArrayList <Event> eventsOnDateWithMatchingCategory = new ArrayList <Event> ();
		for (Event event: eventsOnDate)
		{
			if (academic && event.isAcademic())
				eventsOnDateWithMatchingCategory.add(event);
			if (professional && event.isProfessional())
				eventsOnDateWithMatchingCategory.add(event);
			if (social && event.isSocial())
				eventsOnDateWithMatchingCategory.add(event);
		}
		return eventsOnDateWithMatchingCategory;
	}
	
	public static ArrayList <Event> searchEvents (String keyword)
	{
		String urlValue = "http://team1.appjam.roboteater.com/search.php?keyword="+keyword;
		Log.v("Url value", urlValue);
		ServerEventTask set = new ServerEventTask();
		set.execute(new String [] {urlValue});	
		String result = "";
		try {
			result = set.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		return parseString (result);
	}
	
	/**
	 * Adds an event into the Sheet1 table
	 * @param eventDetails - event details must be in the following order
	 * eventName
	 * date
	 * startTime
	 * endTime
	 * description
	 * location
	 * academic
	 * social
	 * professional
	 * @return
	 */
	public static boolean addEvent (ArrayList <String> eventDetails)
	{
		boolean eventAdded = true;
		String [] categories = {"eventName", "date", "startTime", "endTime", "description", "location", "academic", "social", "professional"};
		String urlValue = "http://team1.appjam.roboteater.com/submitEvent.php?";
		int i =0;
		for (String eventDetail: eventDetails)
		{
			if (eventDetail.isEmpty())
			{
				eventAdded = false;
				break; //none of the fields can be empty
			}
			urlValue += categories[i++] + "=" + eventDetail + "&";
		}
		if (eventDetails.get(6).equals("0") && eventDetails.get(7).equals("0") &&
				eventDetails.get(8).equals("0"))
		{
			eventAdded = false;
		}
		urlValue = urlValue.substring(0, urlValue.length()-1);
		urlValue = urlValue.replace(" ", "%20");
		ServerEventTask set = new ServerEventTask();
		set.execute(new String [] {urlValue});
		return eventAdded;
	}
	
	//Parses the string returned from the SQL script into an ArrayList of Events
	private static ArrayList <Event> parseString (String toParse)
	{
		ArrayList <Event> events = new ArrayList <Event> ();
		String [] individualEventList = toParse.split(EVENT_DELIMITER);
    	individualEventList = Arrays.copyOfRange (individualEventList, 0, individualEventList.length-1);
		for (String event: individualEventList)
		{
			events.add(new Event (event.split(EVENT_PART_DELIMITER)));
		}
		return events;
		
	}
	  
	private static class ServerEventTask extends AsyncTask <String, Void, String>
	{
		
		@Override
		protected String doInBackground(String... params) {
			String response = "";
			for (String url : params) {
				try
		    	{
		    		HttpClient httpClient = new DefaultHttpClient();
		    		HttpPost httpPost = 
		    			new HttpPost(url);
		    		//Url was hardcoded in
//		    		httpPost.setEntity (new UrlEncodedFormEntity(namePairs));
		    		HttpResponse httpresponse = httpClient.execute(httpPost);
		    		HttpEntity entity = httpresponse.getEntity();
		    		InputStream is = entity.getContent();
		    		BufferedReader reader = 
		    			new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                    sb.append(line + "\n");
		            }
		            is.close();
		            response=sb.toString();
		    	}
		    	catch (Exception e)
		    	{
		    		System.err.println (e.getMessage());
		    	}
			}
			return response;
		}	
	}
	
	public String toString ()
	{
		String output = "";
		String newline = "\n";
		output += "Event name: " + this.name;
		output += newline;
		output += "Event location: " + location;
		output += newline;
		this.date.setYear(this.date.getYear() - 1900);
		output += "Event date: " + this.date.toString();
		this.date.setYear(this.date.getYear() + 1900);
		output += newline;
		output += "Event start time-end time: " + startTime + "-" + endTime;
		output += newline;
		output += "Event description: " + description;
		return output;
	}
}
