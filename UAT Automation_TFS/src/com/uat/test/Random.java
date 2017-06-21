package com.uat.test;

import org.testng.annotations.Test;

import com.uat.base.TestBase;

public class Random extends TestBase {

	
	
	
	
		@Test
	public void test() throws Exception
	{
		
			
		String s1 = "01";
		int i = Integer.parseInt(s1);
		System.out.println(i);
		/*DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		String[] text = new String[3];
		
		text=dateFormat.format(cal.getTime()).split("/");
		
		for (int i = 0; i < text.length; i++) {
			System.out.println(text[i]);
		}*/
		//System.out.println(dateFormat.format(cal.getTime()).split("/"));
		
		
		
	}
	
	

}
