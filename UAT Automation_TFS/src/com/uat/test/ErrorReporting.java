package com.uat.test;

import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class ErrorReporting {
	
	
		
	@Test(dataProvider="getData")
	public void errorTesting1(String name){
		
		String expectedName = "Sapan";
		
		try 
		{
			Assert.assertEquals(name, expectedName);
		} 
		catch (Throwable t) 
		{
			ErrorUtil.addVerificationFailure(t);
			System.out.println("exception");
		}
		
		System.out.println("after try/catch");
		
	}
	
	
	/*@Test
	public void errorTesting2(){
		
	}*/
	
	@DataProvider
	public Object[][] getData(){
		
		Object[][] data = new Object[2][1];
		
		data[0][0]="Sapan";
		data[1][0]="Ekta";
		
		return data;
	}

}
