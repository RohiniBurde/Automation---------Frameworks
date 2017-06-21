package com.uat.suite.tm_testcases;

import java.util.ArrayList;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class GetUsersFunctionality extends TestSuiteBase {
	
	ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	static int numOfTestManagers=2;
	static int flag=0;
	
	
	@Test(dataProvider="getData")
	public void testingGetUsers(String role, String totalStakeholders) throws Exception
	{
		
		
		int stakeholders = Integer.parseInt(totalStakeholders);
		
		//int TestManagers = Integer.parseInt(totalTestManagers);
		
		testManager = new ArrayList<Credentials>();
		
		testManager = getUsers("Test Manager", numOfTestManagers);
		
		stakeholder = new ArrayList<Credentials>();
		
		stakeholder = getUsers("Stakeholder", stakeholders);
		
		/*if (stakeholder!=null) 
		{
			
			for (int i = 0; i < stakeholder.size(); i++) {
				
				
				
			}
		}
		else {
		}*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		if (testManager!=null) 
		{
			
			for (int i = 0; i < testManager.size(); i++) 
			{
				
				System.out.println(testManager.get(i).username+"          "+testManager.get(i).password);
								
			}
		}
		else 
		{
			System.out.println("testManager is "+testManager);
		}*/
		if (flag==0) 
		{
			
			openBrowser();
			
			
			closeBrowser();
			
			flag++;
		}
		else 
		{
			
			openBrowser();
			
			
			closeBrowser();
		}
			
			
			
			
		
		
		
		
	}
	
	
	
	
	@DataProvider
	public Object[][] getData(){
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName());
	}

}
