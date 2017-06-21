package com.uat.base;


import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.uat.util.ErrorUtil;
import com.uat.util.TestUtil;
import com.uat.util.Xls_Reader;

public class TestBase {
	public static Logger APP_LOGS=null;
	public static Properties CONFIG=null;
	public static Properties OR=null;
	public static Xls_Reader suiteXls=null;
	public static Xls_Reader TM_Project_Suite_xls=null;
	public static Xls_Reader userXls =null;
	
	public static boolean isInitalized=false;
	public static boolean isBrowserOpened=false;
	public static Hashtable<String,String> sessionData = new Hashtable<String,String>();

	public static WebDriver driver =null;
	public static EventFiringWebDriver eventfiringdriver= null;
	public static WebDriverWait wait;
	
	
	
	// initializing the Tests
	public void initialize() throws Exception{
		// logs
		if(!isInitalized){
		APP_LOGS = Logger.getLogger("devpinoyLogger");
		// config
		APP_LOGS.debug("Loading Property files");
		CONFIG = new Properties();
		FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"//src//com//uat//config/config.properties");
		CONFIG.load(ip);
			
		OR = new Properties();
		ip = new FileInputStream(System.getProperty("user.dir")+"//src//com//uat//config/OR.properties");
		OR.load(ip);
		APP_LOGS.debug("Loaded Property files successfully");
		APP_LOGS.debug("Loading XLS Files");

		// xls file
		TM_Project_Suite_xls = new Xls_Reader(System.getProperty("user.dir")+"\\src\\com\\uat\\xls\\TM_Project Suite.xlsx");
		
		suiteXls = new Xls_Reader(System.getProperty("user.dir")+"//src//com//uat/xls//Suite.xlsx");
		
		userXls=new Xls_Reader(System.getProperty("user.dir")+"//src//com//uat/xls//UAT Users.xlsx");
		APP_LOGS.debug("Loaded XLS Files successfully");
		isInitalized=true;
		}
		
	
	}
	
	// selenium RC/ Webdriver
	// open a browser if its not opened
	public void openBrowser() throws Exception{
	if(!isBrowserOpened){
		if (CONFIG.getProperty("browserType").equals("IE"))
		{	
			 System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\com\\uat\\config\\IEDriverServer.exe");
			 driver = new InternetExplorerDriver();
		}    
		else if(CONFIG.getProperty("browserType").equals("MOZILLA"))
		{	
			driver = new FirefoxDriver();
		}
		else if (CONFIG.getProperty("browserType").equals("CHROME"))
			 driver = new ChromeDriver();
		
		isBrowserOpened=true;
		eventfiringdriver = new EventFiringWebDriver(driver);//Added By Swati Gangrade 30/10/2014
		eventfiringdriver.manage().window().maximize();
		String waitTime=CONFIG.getProperty("default_implicitWait");
		eventfiringdriver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
		wait = new WebDriverWait(eventfiringdriver, 300);
		
		
		  
		 
	}

	}
	
	// close browser
	public void closeBrowser(){
		driver.quit();
		isBrowserOpened=false;
	}
	
	// compare titles
	public boolean compareTitle(String expectedVal){
		try{
			Assert.assertEquals(driver.getTitle() , expectedVal   );
			}catch(Throwable t){
				ErrorUtil.addVerificationFailure(t);			
				APP_LOGS.debug("Titles do not match");
				return false;
			}
		return true;
	}
	
	// compaerStrings
	// compare titles
		public boolean compareNumbers(int expectedVal, int actualValue){
			try{
				Assert.assertEquals(actualValue,expectedVal   );
				}catch(Throwable t){
					ErrorUtil.addVerificationFailure(t);			
					APP_LOGS.debug("Values do not match");
					return false;
				}
			return true;
		}
		
		/*public boolean checkElementPresence(String xpathKey){
			int count =driver.findElements(By.xpath(OR.getProperty(xpathKey))).size();
			
			try{
			Assert.assertTrue(count>0, "No element present");
			}catch(Throwable t){
				ErrorUtil.addVerificationFailure(t);			
				APP_LOGS.debug("No element present");
				return false;
			}
			return true;
		}
		*/
		
	/*	public WebElement getObject(String xpathKey){
			
			try{
			WebElement x = driver.findElement(By.xpath(OR.getProperty(xpathKey)));
			return x;
			}catch(Throwable t){
				// report error
				ErrorUtil.addVerificationFailure(t);			
				APP_LOGS.debug("Cannot find object with key -- " +xpathKey);
				return null;
			}
			
		}*/
		
		
		
		
		
		
		
		/**
		 * This function opens the site defined in the config.properties file and logins based on role provided for which user is fetched from User Xls file.
		 * Note that the first user present in the Xls file, with the role passed in this function, is used to login.
		 * If the role is not present in Xls file or if proper role name is not passed to the function, then closeBrowser() function is called which quits the driver.
		 *  
		 * @param role		role based on User Xls file
		 * @return 			returns true if login is successfull and role matches on the site, otherwise returns false if role is not present in xls or if role doesn't match on the site
		 */
		public boolean login(String role){
			
			Object actualSiteRole= null;
			String username;
			String password;
			String xlsRole;
			
			try
			{
				if (!role.isEmpty()) 
				{
					
					for(int i =2 ; i<=userXls.getRowCount("Users") ; i++)
					{

						
						xlsRole=userXls.getCellData("Users", "Role", i);
						
						if(role.equalsIgnoreCase(xlsRole) )
						{
							APP_LOGS.debug("Role found in Users file. Fetching credentials");
							
							username = userXls.getCellData("Users", "Username", i);
							
							password = userXls.getCellData("Users", "Password", i);
							
							eventfiringdriver.get(CONFIG.getProperty("siteUrl"));
														
							UATLoginWindow(username, password);
							
							Thread.sleep(3000);
							
							actualSiteRole = (Object) eventfiringdriver.executeScript("return security.userType");
							
							APP_LOGS.debug("Comparing selected role and site role");
							
							
							if(actualSiteRole.toString().contains(CONFIG.getProperty(xlsRole.replace(' ', '_'))))
							{
								
								APP_LOGS.debug("Role Matched");
								return true;
								
							}
							else 
							{
							
								APP_LOGS.debug("Role did not Match. Quitting driver");
								
								closeBrowser();
								
								return false;
																
							}
															
						}
						
					}
					
					APP_LOGS.debug("Role not found in Xls file. Quitting driver");
					
					closeBrowser();
					
					return false;
					
				}
				else 
				{
					APP_LOGS.debug("Role not provided. Quitting driver");
					closeBrowser();
					return false;
				}
				
				
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				APP_LOGS.debug("Exception occured in login function. Quitting driver");
				closeBrowser();
				return false;
				
			}
			
		
		}
		
		/**
		 * This function opens the site defined in the config.properties file and logins based on username and password provided and matches the role with the actual site role.
		 * If proper role name is not passed to the function, then closeBrowser() function is called which quits the driver and false is returned.
		 *  
		 * @param username	username of user with access to the site
		 * @param password	password of the user
		 * @param role		role from the following 'Admin' 'Version Lead' 'Test Manager' 'Tester' 'Stakeholder'. Roles are case sensitive
		 * @return 			returns true if login is successfull and role matches on the site, otherwise returns false in any other condition
		 */
		public boolean login(String username, String password, String role){
			
			Object actualSiteRole= null;
			String siteRoleId = CONFIG.getProperty(role.replace(" ", "_"));
			if (!(siteRoleId==null)) {
				
				try
				{
					
					APP_LOGS.debug("Role is not correct. Quitting driver");
					
					eventfiringdriver.get(CONFIG.getProperty("siteUrl"));
					
					UATLoginWindow(username, password);
					
					Thread.sleep(3000);
					
					actualSiteRole = (Object) eventfiringdriver.executeScript("return security.userType");
					
					APP_LOGS.debug("Comparing passed role and site role");
					
					
					if(actualSiteRole.toString().contains(siteRoleId))
					{
						
						APP_LOGS.debug("Role Matched");
						return true;
						
					}
					else 
					{
					
						APP_LOGS.debug("Role did not Match. Quitting driver");
						
						closeBrowser();
						
						return false;
														
					}	
					
					
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					APP_LOGS.debug("Exception occured in login function. Quitting driver");
					closeBrowser();
					return false;
					
				}
				
			}
			else {
				APP_LOGS.debug("Role is not present. Quitting Driver");
				closeBrowser();
				return false;
			}
			
			
		
		}
		
		//code added by Sapan
		private void UATLoginWindow(String username, String password) throws InterruptedException, AWTException
		{
		
			try
			{
				APP_LOGS.debug("On Login Window");
				Alert a = eventfiringdriver.switchTo().alert();
		        a.sendKeys(username);   
		        
		       	Robot r = new Robot();
		        r.keyPress(KeyEvent.VK_ENTER);                    
		        r.keyPress(KeyEvent.VK_ENTER);   
		        
		        Thread.sleep(2000);
		        Alert b = eventfiringdriver.switchTo().alert();
		        b.sendKeys(password);
		    
		        r.keyPress(KeyEvent.VK_ENTER);
		        r.keyPress(KeyEvent.VK_ENTER);
		        
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				
				APP_LOGS.debug("Login failed at Login Window. Quitting Driver");
				closeBrowser();
				
			}
			
			
		}
		
		/**
		 * This function will get all the user credentials based on the role passed to it from User Xls file.
		 * Note that it returns ArrayList of a custom class type Credentials. So the reference variable, calling this function to get all desired role users, should be an ArrayList of type Credentials.
		 * After getting all the users in your ArrayList, use the statement 'variable_name.get(index).username' without quotes to get username for the provided index and for password just replace 'username' in the statement with 'password'.
		 * 
		 * @param role		role based on User Xls file.
		 * @param numOfUsers	number of uers to fetch for the enterd role.
		 * @return			returns ArrayList of type Credentials if users are present for the provided role else returns null in any other condition.
		 * @see				Credentials 
		 */		
		public ArrayList<Credentials> getUsers(String role, int numOfUsers){
			
			int numOfRows;
			String xlsRole;
			ArrayList<Credentials> user = new ArrayList<Credentials>();
			int index =0;
			String sheetName = "Role";
			
			
			if (!role.isEmpty()) {
				
				numOfRows = userXls.getRowCount(sheetName);
				
				for (int i = 2; i <= numOfRows; i++) {
					
					xlsRole = userXls.getCellData(sheetName, "Role", i);
					
					if (role.equalsIgnoreCase(xlsRole)) {
						
						user.add(new Credentials());
						
						user.get(index).username= userXls.getCellData(sheetName, "Username", i);
						
						user.get(index).password= userXls.getCellData(sheetName, "Password", i);
						
						index++;
						
						if (index==numOfUsers) {
							
							break;
							
						}
						
					}
					
				}
				
				if (user.size()!=0) {
					
					return user;
					
				}
				else {
					
					return null;
				}
				
			}
			else {
				return null;
			}
			
		}
		
		
		//reading xpath of element from object repository file
		  public WebElement getObject(String xpathKey)
		  {
		   try
		   {
		    WebElement x = eventfiringdriver.findElement(By.xpath(OR.getProperty(xpathKey)));
		    return x;
		   }catch(Throwable t)
		   {
		    // report error
		    ErrorUtil.addVerificationFailure(t);   
		    APP_LOGS.debug("Cannot find object with key -- " +xpathKey);
		    return null;
		   }
		   
		  }
		  
		       
		  //reading id of element from object repository file
		  public static WebElement getElement (String id) throws IOException
		  {
		     try{
		           return eventfiringdriver.findElement(By.id(OR.getProperty(id)));
		     }
		     catch(Throwable t)
		     {
		       //report error     
		    ErrorUtil.addVerificationFailure(t);   
		    APP_LOGS.debug("Cannot find element with key -- " +id);
		    return null;
		     }
		  }
		  
		  
		  
		  public WebElement getObject(String xpathKey1,String xpathKey2, int index )
		  {
			  
			  String collectiveXpathKey = OR.getProperty(xpathKey1)+index+OR.getProperty(xpathKey2);
			   
			   try
			   {
			    
			    return eventfiringdriver.findElement(By.xpath(collectiveXpathKey));
			    
			   }
			   catch(Throwable t)
			   {
			    // report error
			    ErrorUtil.addVerificationFailure(t);   
			    APP_LOGS.debug("Cannot find object with key -- " +collectiveXpathKey);
			    return null;
			   }
			   
		  }
		  
		  
		  //Added by Swati Gangrade on date 01st Nov 2014 for the selction of month and year from date picker using its class attribute
		  
		  //reading id of element from object repository file
		  
		  public static WebElement getElementByClassAttr (String id) throws IOException
		  {
		     try{
		           return eventfiringdriver.findElement(By.className(OR.getProperty(id)));
		     }
		     catch(Throwable t)
		     {
		       //report error     
		    ErrorUtil.addVerificationFailure(t);   
		    APP_LOGS.debug("Cannot find element with key -- " +id);
		    return null;
		     }
		  }
	
	
	
	
	
	

}
