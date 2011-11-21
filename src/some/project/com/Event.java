package some.project.com;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;


public class Event 
{

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
		ArrayList <Event> matchingEvents = new ArrayList <Event> ();
		String result = "";
		try
    	{
   
    		HttpClient httpClient = new DefaultHttpClient();
    		String urlValue = "http://team1.appjam.roboteater.com/selecter.php?";
    		urlValue += "Category=" + category + "&" + category + "=" + categoryValue;
    		HttpPost httpPost = 
    			new HttpPost(urlValue);
    		//Url was hardcoded in
//    		httpPost.setEntity (new UrlEncodedFormEntity(namePairs));
    		HttpResponse response = httpClient.execute(httpPost);
    		HttpEntity entity = response.getEntity();
    		InputStream is = entity.getContent();
    		BufferedReader reader = 
    			new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            is.close();
     
            result=sb.toString();
    	}
    	catch (Exception e)
    	{
    		System.err.println (e.getMessage());
    	}
    	String [] individualEventList = result.split("NEXT_EVENT");
    	//Array contained null elements at the end that threw and exception
    	//Array is therefore parsed to remove the last element
    	individualEventList = Arrays.copyOfRange (individualEventList, 0, individualEventList.length-1);
    	for (String event: individualEventList)
    	{
    		matchingEvents.add (new Event(event.split("##")));
    	}
    	return matchingEvents;		
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
		
		urlValue = urlValue.substring(0, urlValue.length()-1);
		urlValue = urlValue.replace(" ", "%20");
		AddEventTask aet = new AddEventTask();
		aet.execute(new String [] {urlValue});
		return eventAdded;
	}
	
	private static class AddEventTask extends AsyncTask <String, Void, String>
	{
		
		@Override
		protected String doInBackground(String... params) {
			String response = "";
			for (String url : params) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				try 
				{
					HttpResponse execute = client.execute(httpPost);
				}
				catch (Exception e) {
					Log.v("Error In Executing Client Post", e == null ? "null": e.getMessage());
				}
			}
			return response;
		}	
	}
}
