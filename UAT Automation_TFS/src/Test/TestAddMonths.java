package Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.DateFormatSymbols;


public class TestAddMonths {

	public static void main(String[] args) 
	{
		System.out.println(" Executing Test Case -> ");	
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
		
		System.out.println(addMonthInSysdate(date, 1));
		

		System.out.println(dateFormat.format(addMonthInSysdate(date, 1)));
		
		String requiredDate = dateFormat.format(addMonthInSysdate(date, 1));
		
		String[] dateMonthYear = requiredDate.split("/");
		String endMonth;
		int endDate;
		int endYear;
		
		endMonth = dateMonthYear[0];
		getMonth(Integer.parseInt(endMonth));
		System.out.println(getMonth(Integer.parseInt(endMonth)));
		
		endDate = Integer.parseInt(dateMonthYear[1]);
		System.out.println("endDate "+ dateMonthYear[1]);
		
		endYear = Integer.parseInt(dateMonthYear[2]);
		System.out.println("endYear "+ dateMonthYear[2]);
		
		
	}
	public static Date addMonthInSysdate(Date date, int numberOfMonthsToBeAdded) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTimeInMillis(date.getTime());
	    cal.add(Calendar.YEAR, numberOfMonthsToBeAdded);
	    return new Date(cal.getTimeInMillis());
	}
	
	public static String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
}
