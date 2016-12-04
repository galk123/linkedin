package linkedinapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Experience {
	String title;
	String subTitle;
	String dateRange;
	String description;
	double experienceInYears;
	
	public Experience(String title, String subTitle, String dateRange, String description){
		this.title = title;
		this.subTitle = subTitle;
		this.dateRange = dateRange;
		this.description = description;
		if(dateRange != null){
			String start = dateRange.substring(0, dateRange.indexOf(" – "));
			if(start.length() > 4){
				String end = dateRange.substring(dateRange.indexOf(" – ")+3, dateRange.indexOf(" ("));
				SimpleDateFormat parser = new SimpleDateFormat("MMM yyyy");
		        try {
		        	long milisec = parser.parse(end).getTime() - parser.parse(start).getTime();
		        	double hours = milisec / 3600000;
		        	experienceInYears = hours / 8760;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				if(dateRange.contains("less than a year"))
					experienceInYears = 0.5;
				else{
					experienceInYears = Double.valueOf(dateRange.substring(dateRange.indexOf('(')+1, dateRange.indexOf('y')-1));
				}
			}
		}
	}
}
